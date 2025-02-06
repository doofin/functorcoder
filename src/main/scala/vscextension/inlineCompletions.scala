package vscextension
import typings.vscode.mod as vscode

import scala.scalajs.js
import scala.scalajs.js.JSConverters.*

import facade.vscodeUtils.*

/** demonstrates how to provide inline completions in the editor. like the github copilot
  * https://github.com/microsoft/vscode-extension-samples/tree/main/inline-completions
  * https://github.com/continuedev/continue/blob/main/core/autocomplete/CompletionProvider.ts
  */
object inlineCompletions {

  def createCompletionProvider(): vscode.InlineCompletionItemProvider = {
    new vscode.InlineCompletionItemProvider {
      override def provideInlineCompletionItems(
          document: vscode.TextDocument, // the current document
          position: vscode.Position, // the position of the cursor
          context: vscode.InlineCompletionContext,
          token: vscode.CancellationToken // to cancel the completion
      ) = {

        val offset = 0
        // get the line before the current line
        val lineBefore =
          if !(position.line - offset < 0)
          then document.lineAt(position.line - offset).text
          else ""

        // the whole line before the cursor
        showMessage(s"line before cursor: $lineBefore")

        // always return a list of items, but only first item will be displayed
        val items = List("foo", "bar", "baz")
          // .filter(_.startsWith(word)) // often need to do some filtering
          .map { str =>
            new vscode.InlineCompletionItem(
              insertText = str, // text to insert
              range = new vscode.Range(position, position)
            )
          }

        // return a promise of the items, useful for async but not needed here
        val providerResultF =
          jsUtils.newJsPromise(items.toJSArray)

        providerResultF.asInstanceOf[typings.vscode.mod.ProviderResult[
          scala.scalajs.js.Array[typings.vscode.mod.InlineCompletionItem] | typings.vscode.mod.InlineCompletionList
        ]]
      }

    }
  }

  def registerInlineCompletions() = {
    vscode.languages.registerInlineCompletionItemProvider(selector = "*", provider = createCompletionProvider())
  }
}
