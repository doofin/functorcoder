package vscextension

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import typings.vscode.mod as vscode

import facade.vscodeUtils.*

/** Show a quick pick palette to select items in multiple steps
  *
  * similar to the command palette in vscode
  */
object quickPick {

  def showQuickPick(): Unit = {
    val items =
      js.Array("item1", "item2", "item3")
    js.Dynamic.literal(
      placeHolder = "pick one item"
    )

    val quickPick: vscode.QuickPick[vscode.QuickPickItem] =
      vscode.window.createQuickPick()

    var steps = 0 // steps for the quick pick
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
      steps += 1
      quickPick.setStep(steps)
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
    /* quickPick.onDidHide({ () =>
      quickPick.dispose()
    }) */
    quickPick.show()
  }
}
