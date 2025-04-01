package vscextension.facade

import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js

import typings.vscode.mod as vscode
import typings.vscode.mod.Command

/** a dialog in the editor that users can accept or reject
  *
  * part of the
  *
  * https://github.com/microsoft/vscode/blob/main/src/vscode-dts/vscode.proposed.inlineEdit.d.ts
  */

object InlineEdit {

  @js.native
  @JSImport("vscode", "InlineEdit")
  class InlineEdit extends js.Object {
    def this(text: String, range: vscode.Selection) = this()
    val text: String = js.native
    val range: vscode.Selection = js.native

    val showRange: Range = js.native
    val accepted: Command = js.native
    val rejected: Command = js.native
    val shown: Command = js.native
    val commands: Command = js.native
    val action: Command = js.native
  }

  @js.native
  trait InlineEditContext extends js.Object {
    val triggerKind: vscode.CodeActionTriggerKind = js.native
  }

//   @js.native
  trait InlineEditProvider extends js.Object {
    def provideInlineEdits(
        document: vscode.TextDocument,
        content: InlineEditContext,
        token: vscode.CancellationToken
    ): js.Promise[js.Array[InlineEdit]]
  }

  @JSImport("vscode", "languages")
  @js.native
  object languages extends js.Object {
    def registerInlineEditProvider(selector: vscode.DocumentSelector, provider: InlineEditProvider): vscode.Disposable =
      js.native
  }

}
