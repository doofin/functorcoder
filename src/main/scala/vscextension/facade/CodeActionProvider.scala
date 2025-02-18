package vscextension
import typings.vscode.mod as vscode
import scala.scalajs.js

import facade.vscodeUtils.*
import functorcoder.llm.llmMain.llmAgent
import functorcoder.llm.llmPrompt
import scala.concurrent.Future
import functorcoder.types.editorCtx.*

/** Code actions are commands provided at the cursor in the editor, so users can
  *
  * quickly fix issues or refactor code, etc.
  *
  * https://github.com/microsoft/vscode-extension-samples/blob/main/code-actions-sample/README.md
  *
  * manually created facade due to scalablytyped issue
  */
trait CodeActionProvider {
  def provideCodeActions(
      document: vscode.TextDocument,
      range: vscode.Selection,
      context: vscode.CodeActionContext,
      token: vscode.CancellationToken
  ): vscode.ProviderResult[js.Array[vscode.CodeAction]]
}
