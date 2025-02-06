package vscextension

import typings.vscode.mod as vscode

import scala.collection.immutable
import scala.scalajs.js

import facade.vscodeUtils.*

/** Commands are actions that a user can invoke in the vscode extension with command palette (ctrl+shift+p).
  *
  * This object registers all the commands in the extension.
  */
object commands {
  // Store all the commands here
  def registerAllCommands(context: vscode.ExtensionContext) = {
    val cmds =
      Seq(
        ("extension.helloWorld", showHello)
      )

      // register the commands
    cmds foreach { (name, fun) =>
      context.pushDisposable(
        vscode.commands.registerCommand(name, fun)
      )
    }
  }

  /** Example command. VSCode commands can take an argument of any type, hence the `Any` here.
    *
    * @param arg
    *   the argument (we don't use, but could be useful for other commands)
    */
  def showHello(arg: Any): Unit = {
    // show a message box when the command is executed in command palette
    // by typing hello
    vscode.window.showInformationMessage(s"Hello World! How are you ?")
  }
}
