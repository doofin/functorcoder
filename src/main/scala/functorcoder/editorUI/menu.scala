package functorcoder.editorUI

import typings.vscode.mod as vscode
import vscextension.facade.vscodeUtils.*
import vscextension.quickPick
import functorcoder.llm.llmMain.llmAgent
import functorcoder.actions.Commands
import cats.syntax.show

object menu {
  case class Menu(
      title: String, //
      menuItems: Seq[(String, () => Unit)]
  )

  // the main menu
  def getMainMenu(llm: llmAgent) = {
    val mainMenuItems: Seq[(String, () => Unit)] = Seq(
      "create files" -> { () =>
        // invoke the create files command directly as function
        val _: Unit = Commands.cmdCreateFiles._2(llm)(())
      },
      "disable autocomplete" -> { () => showMessageAndLog("disable autocomplete") }
    )
    Menu(
      title = "functorcoder menu",
      menuItems = mainMenuItems
    )
  }
}
