package vscextension

import typings.vscode.mod as vscode

import scala.scalajs.js

import facade.vscodeUtils.*
import functorcoder.llm.llmMain.llmAgent

/** Commands are actions that a user can invoke in the vscode extension with command palette (ctrl+shift+p).
  *
  * This object registers all the commands in the extension.
  */
object vscCommands {

  /** Register all the commands in the extension.
    *
    * @param context
    *   the vscode extension context
    */
  def registerAllCommands(context: vscode.ExtensionContext, llm: llmAgent) = {

    val allCommands =
      functorcoder.actions.Commands.commandList(llm)
    // register the commands
    allCommands foreach { (name, fun) =>
      context.pushDisposable(
        vscode.commands.registerCommand(name, fun)
      )
    }
  }

}
