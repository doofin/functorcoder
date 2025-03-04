package vscextension

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.JSConverters._

import typings.vscode.mod as vscode

import facade.vscodeUtils.*
import functorcoder.actions.Commands

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
    val items =
      Commands.mainMenuItems.map(_._1).toJSArray

    val quickPick: vscode.QuickPick[vscode.QuickPickItem] =
      vscode.window.createQuickPick()

    quickPick.title = "Quick Pick"
    quickPick.placeholder = "pick one item"
    quickPick.totalSteps = 3
    quickPick.buttons = js.Array(vscode.QuickInputButtons.Back)

    // option items for user to pick
    quickPick.items = items.map { itemStr => //
      vscode
        .QuickPickItem(itemStr)
        .setAlwaysShow(true)
        .setButtons(js.Array(vscode.QuickInputButtons.Back))
        .setDescription(itemStr + " description")
        .setDetail(itemStr + " detail")
    }

    quickPick.onDidChangeSelection { selection =>
      println(s"selected: ${selection(0).label}")
      if (selection(0).label == "item1") {
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

      }
    }

    quickPick.onDidHide({ _ =>
      quickPick.hide()
    })

    quickPick.show()
  }

}
