package vscextension

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

import typings.vscode.mod as vscode

object extensionMain {

  // The main entry for the extension, called when editor opens
  @JSExportTopLevel("activate")
  def activate(context: vscode.ExtensionContext): Unit = {

    // vscode.workspace.rootPath.getOrElse("")
    val cfg = vscConfig.readConfig()
    val llm = functorcoder.llm.llmMain.llmAgent(cfg)

    // register all commands
    vscCommands.registerAllCommands(context, llm)

    // show the status bar
    statusBar.createStatusBarItem(context, llm)

    // register inline completions like github copilot
    inlineCompletions.registerInlineCompletions(llm)

    // quick pick palette, like command palette
    // quickPick.showQuickPick()

    // code actions like quick fixes
    CodeActions.registerCodeActions(context, llm)

  }

}
