package functorcoder.editorUI

import typings.vscode.mod as vscode
import vscextension.facade.vscodeUtils.*
import vscextension.quickPick

object menu {
  case class Menu(
      title: String, //
      menuItems: Seq[(String, () => Unit)]
  )
  // the menu items
  val mainMenuItems: Seq[(String, () => Unit)] = Seq(
    "create files" -> { () =>
      quickPick.createInputBox(
        title = "Create files/folders description",
        placeHolder = "describe your project",
        onInput = { input =>
          showMessageAndLog("input: " + input)
        }
      )

    },
    "disable autocomplete" -> { () => showMessageAndLog("disable autocomplete") }
  )

  // the main menu
  val myMenu = Menu(
    title = "functorcoder menu",
    menuItems = mainMenuItems
  )
}
