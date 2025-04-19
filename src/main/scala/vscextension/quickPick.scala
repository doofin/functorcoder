package vscextension

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.concurrent.ExecutionContext.Implicits.global

import typings.vscode.mod as vscode
import functorcoder.llm.llmMain.llmAgent

/** Show a quick pick palette to select items in multiple steps
  *
  * similar to the command palette in vscode
  */
object quickPick {

  /** create a quick pick with a list of items
    *
    * @param title
    *   the title of the quick pick
    * @param placeHolder
    *   the placeholder text
    * @param items
    *   (label, description, function) a list of items to show in the quick pick
    * @param modifieF
    *   a function to modify the quick pick (e.g. add buttons)
    */
  def createQuickPick(
      title: String, //
      placeHolder: String,
      items: Seq[(String, String, () => Unit)],
      modifieF: vscode.QuickPick[vscode.QuickPickItem] => Unit = { _ => }
  ) = {

    val quickPick: vscode.QuickPick[vscode.QuickPickItem] =
      vscode.window.createQuickPick()

    quickPick.title = title
    quickPick.placeholder = placeHolder
    // to customize the quick pick
    modifieF(quickPick)
    quickPick.buttons = js.Array(vscode.QuickInputButtons.Back)

    quickPick.items = items.map { (itemStr, itemDesc, _) => //
      vscode
        .QuickPickItem(itemStr)
        .setAlwaysShow(true)
        .setButtons(js.Array(vscode.QuickInputButtons.Back))
        .setDetail(itemDesc)
    }.toJSArray

    quickPick.onDidChangeSelection { selection =>
      println(s"selected: ${selection(0).label}")
      // execute the function associated with the selected item
      val selected = items.find(_._1 == selection(0).label)
      selected.foreach { (_, _, fun) =>
        fun()
        quickPick.hide()
      }
    }

    quickPick.onDidHide({ _ =>
      quickPick.hide()
    })

    quickPick.show()
    quickPick
  }

  def showMainMenu(llm: llmAgent)(arg: Any): Unit = {
    val mMenu = functorcoder.editorUI.menu.getMainMenu(llm)

    createQuickPick(
      title = mMenu.title,
      placeHolder = "select an action",
      items = mMenu.menuItems.map(x => (x._1, x._1, x._2)).toSeq
    )
  }

  /** create an input box for string
    *
    * @param title
    *   the title of the input box
    * @param placeHolder
    *   the placeholder text
    * @param onInput
    *   the function to call when input is received
    */
  def createInputBox(
      title: String,
      placeHolder: String,
      onInput: String => Unit
  ) = {
    val inputBoxOptions =
      vscode
        .InputBoxOptions()
        .setTitle(title)
        .setPlaceHolder(placeHolder)

    vscode.window.showInputBox(inputBoxOptions).toFuture.foreach { inputO =>
      inputO.toOption match {
        case None        =>
        case Some(input) => onInput(input)
      }
    }
  }
}
