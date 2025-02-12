package vscextension

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

import typings.vscode.mod as vscode

import facade.vscodeUtils.*

object extensionMain {

  /** The main entry for the extension, called when activated first time.
    */
  @JSExportTopLevel("activate") // Exports the function to javascript so that VSCode can load it
  def activate(context: vscode.ExtensionContext): Unit = {
    showMessageAndLog("congrats, your scala.js vscode extension is loaded")

    vscode.workspace.rootPath.getOrElse("")
    val cfg = settings.readConfig()
    showMessageAndLog(s"config loaded: ${cfg.toString()}")
    // register all commands
    commands.registerAllCommands(context)

    // show the status bar
    val statusBarItem =
      statusBar.createStatusBarItem(context)
    // statusBarItem.text = "functorcoder ok"
    // show the current language of the document
    documentProps.showProps

    // register inline completions like github copilot
    inlineCompletions.registerInlineCompletions()

    // quick pick palette, like command palette
    // quickPick.showQuickPick()

    // code actions like quick fixes
    CodeActions.registerCodeActions(context)

    // functorcoder.llm.llmAIMain.test
    // file operations
    // io.fileIO.createFile(projectRoot)
    // load configuration
    // val cfg = io.config.loadConfig(projectRoot + "/.vscode/settings.json")
    // showMessageAndLog(s"config loaded: $cfg")

    // language server client
    // lsp.startLsp()

    // webview
    // webview.showWebviewPanel()

    // editor config

  }

}
