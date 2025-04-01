package vscextension

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

import typings.vscode.mod as vscode


/** Show a quick pick palette to select items in multiple steps
  *
  * similar to the command palette in vscode
  */
object quickPick {

  def createQuickPick(
      title: String, //
      items: Seq[(String, String, () => Unit)],
      modifies: vscode.QuickPick[vscode.QuickPickItem] => Unit = { _ => }
  ) = {

    val quickPick: vscode.QuickPick[vscode.QuickPickItem] =
      vscode.window.createQuickPick()

    quickPick.title = title
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

  def showQuickPick(arg: Any): Unit = {
    val mMenu = functorcoder.editorUI.menu.myMenu

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

      /* if (selection(0).label == "item1") {
        println(s"selected: ${selection(0).label}")

        // show another input box after selecting item1
        val inputBoxOptions =
          vscode
            .InputBoxOptions()
            .setTitle("Input Box")
            .setPlaceHolder("type something")

        vscode.window.showInputBox(inputBoxOptions).toFuture.foreach { input =>
          showMessage("input: " + input)
        }

      } */

    }

    quickPick.onDidHide({ _ =>
      quickPick.hide()
    })

    quickPick.show()
  }

}
