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

  def createQuickPick(
      title: String, //
      placeHolder: String,
      items: Seq[(String, String, () => Unit)],
      modifies: vscode.QuickPick[vscode.QuickPickItem] => Unit = { _ => }
  ) = {

    val quickPick: vscode.QuickPick[vscode.QuickPickItem] =
      vscode.window.createQuickPick()

    quickPick.title = title
    quickPick.placeholder = placeHolder
    // to customize the quick pick
    modifies(quickPick)
    quickPick.buttons = js.Array(vscode.QuickInputButtons.Back)

    quickPick.items = items.toJSArray.map { (itemStr, itemDesc, _) => //
      vscode
        .QuickPickItem(itemStr)
        .setAlwaysShow(true)
        .setButtons(js.Array(vscode.QuickInputButtons.Back))
        .setDescription(itemStr + " description")
        .setDetail(itemDesc + " detail")
    }

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
  }

  def showMainMenu(llm: llmAgent)(arg: Any): Unit = {
    val mMenu = functorcoder.editorUI.menu.getMainMenu(llm)

    val quickPick: vscode.QuickPick[vscode.QuickPickItem] =
      vscode.window.createQuickPick()

    quickPick.title = mMenu.title
    quickPick.placeholder = "select an action"
    // quickPick.totalSteps = 3
    quickPick.buttons = js.Array(vscode.QuickInputButtons.Back)

    // set the items in the quick pick
    quickPick.items = mMenu.menuItems.map(_._1).toJSArray.map { itemStr => //
      vscode
        .QuickPickItem(itemStr) // label is itemStr
        // .setAlwaysShow(true)
        // .setButtons(js.Array(vscode.QuickInputButtons.Back))
        .setDescription(itemStr)
      // .setDetail(itemStr + " detail")
    }

    quickPick.onDidChangeSelection { selection =>
      val selectedLabel = selection(0).label
      // execute the function associated with the selected item
      mMenu.menuItems.find(_._1 == selectedLabel).foreach { (_, fun) =>
        fun()
        quickPick.hide()
      }

    }

    quickPick.onDidHide({ _ =>
      quickPick.hide()
    })

    quickPick.show()
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
