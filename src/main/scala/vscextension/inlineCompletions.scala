package vscextension

import typings.vscode.mod as vscode
import scala.concurrent.ExecutionContext.Implicits.global

import scala.scalajs.js
import scala.scalajs.js.JSConverters.*
import scala.scalajs.js.Promise

import functorcoder.llm.llmMain.llmAgent
import functorcoder.actions.CodeCompletion
import vscextension.facade.vscodeUtils.showMessageAndLog

/** demonstrates how to provide inline completions in the editor. like the github copilot
  * https://github.com/microsoft/vscode-extension-samples/tree/main/inline-completions
  * https://github.com/continuedev/continue/blob/main/core/autocomplete/CompletionProvider.ts
  */
object inlineCompletions {

  def registerInlineCompletions(llm: llmAgent) = {
    val mCompletionProvider = new vscode.InlineCompletionItemProvider {
      override def provideInlineCompletionItems(
          document: vscode.TextDocument, // the current document
          position: vscode.Position, // the position of the cursor
          context: vscode.InlineCompletionContext,
          token: vscode.CancellationToken // to cancel the completion
      ) = {

        val codeBefore = document.getText(new vscode.Range(new vscode.Position(0, 0), position))
        val codeAfter = document.getText(new vscode.Range(position, document.positionAt(document.getText().length)))

        val promptResponseF = CodeCompletion.getCompletion(codeBefore, codeAfter, llm)

        val providerResultF: Promise[scala.scalajs.js.Array[vscode.InlineCompletionItem]] =
          promptResponseF.map { completionText =>
            showMessageAndLog(s"completionText: $completionText")
            js.Array(
              new vscode.InlineCompletionItem(
                insertText = completionText, // text to insert
                range = new vscode.Range(position, position)
              )
            )
          }.toJSPromise

        statusBar.showSpininngStatusBarItem(s"functorcoder(${editorAPI.getLanguage()})", providerResultF)

        providerResultF.asInstanceOf[typings.vscode.mod.ProviderResult[
          scala.scalajs.js.Array[typings.vscode.mod.InlineCompletionItem] | typings.vscode.mod.InlineCompletionList
        ]]
      }

    }
    vscode.languages
      .registerInlineCompletionItemProvider(selector = "*", provider = mCompletionProvider)
  }
}
