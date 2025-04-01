package functorcoder.editorUI

import vscextension.facade.vscodeUtils.*

object menu {
  case class Menu(
      title: String, //
      menuItems: Seq[(String, () => Unit)]
  )
  // the menu items
  val mainMenuItems: Seq[(String, () => Unit)] = Seq(
    "create files" -> { () => showMessageAndLog("create files") },
    "disable autocomplete" -> { () => showMessageAndLog("disable autocomplete") }
  )

  // the main menu
  val myMenu = Menu(
    title = "functorcoder menu",
    menuItems = mainMenuItems
  )
}
