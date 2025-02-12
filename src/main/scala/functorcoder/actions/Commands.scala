package functorcoder.actions

import typings.vscode.mod as vscode

import scala.collection.immutable
import scala.scalajs.js

import vscextension.quickPick

/** Commands are actions that a user can invoke in the vscode extension with command palette (ctrl+shift+p).
  */
object Commands {
  type CommandT = Any => Unit
  // all the commands here
  val commandMenu = "functorcoder.menu"

  val commandList: Seq[(String, CommandT)] =
    Seq(
      (commandMenu, quickPick.showQuickPick)
    )

    // the main menu items
  val mainMenuItems: Seq[(String, () => Unit)] = Seq(
    "create files" -> { () => println("create files") }
  )
}
