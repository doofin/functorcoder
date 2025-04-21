package vscextension

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

import typings.vscode.mod as vscode

object extensionMain {

  /** The main entry for the extension, called when activated first time.
    */
  @JSExportTopLevel("activate") // Exports the function to javascript so that VSCode can load it
  def activate(context: vscode.ExtensionContext): Unit = {
    // showMessageAndLog("congrats, your scala.js vscode extension is loaded")

    // vscode.workspace.rootPath.getOrElse("")
    val cfg = vscConfig.readConfig()
    val llm = functorcoder.llm.llmMain.llmAgent(cfg)

    // showMessageAndLog(s"config loaded: ${cfg.toString()}")
    // register all commands
    vscCommands.registerAllCommands(context, llm)

    // show the status bar
    statusBar.createStatusBarItem(context, llm)
    // statusBarItem.text = "functorcoder ok"
    // show the current language of the document
    // documentProps.showProps

    // register inline completions like github copilot
    inlineCompletions.registerInlineCompletions(llm)

    // quick pick palette, like command palette
    // quickPick.showQuickPick()

    // code actions like quick fixes
    CodeActions.registerCodeActions(context, llm)

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
