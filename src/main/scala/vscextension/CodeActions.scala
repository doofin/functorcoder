package vscextension
import typings.vscode.mod as vscode
import scala.scalajs.js

import facade.vscodeUtils.*

/** Code actions are commands provided at the cursor in the editor, so users can
  *
  * quickly fix issues or refactor code, etc.
  *
  * https://github.com/microsoft/vscode-extension-samples/blob/main/code-actions-sample/README.md
  */
object CodeActions {

  def registerCodeActions(context: vscode.ExtensionContext) = {

    // https://scalablytyped.org/docs/encoding#jsnative
    // we need to manually create the js object and cast it
    val mActionProvider =
      new js.Object {
        def provideCodeActions(
            document: vscode.TextDocument,
            range: vscode.Selection,
            context: vscode.CodeActionContext,
            token: vscode.CancellationToken
        ): js.Array[vscode.CodeAction] = {

          // check who triggers the code action, since vscode may trigger it automatically
          context.triggerKind match {
            case vscode.CodeActionTriggerKind.Invoke =>
              // triggered by user
              showMessageAndLog("selected code: " + document.getText(range))
            case _ =>
          }
          createCodeAction(document, range, context)

          // show an underline for compiler issues
          /* context.diagnostics.map { diagnostic =>
          val codeAction = createCodeAction()
          codeAction
        } */
        }
      }.asInstanceOf[vscode.CodeActionProvider[vscode.CodeAction]]

    val registration: vscode.Disposable = vscode.languages.registerCodeActionsProvider(
      selector = "*",
      provider = mActionProvider,
      metadata = vscode
        .CodeActionProviderMetadata()
        .setProvidedCodeActionKinds(
          js.Array(vscode.CodeActionKind.QuickFix)
        )
    )

    context.pushDisposable(registration)
    showMessageAndLog("registered code actions")
  }

  def createCodeAction(document: vscode.TextDocument, range: vscode.Selection, context: vscode.CodeActionContext) = {

    // create quick fix action item
    val codeActionFix1 =
      new vscode.CodeAction(
        title = "My Code Action- replace with hello string",
        kind = vscode.CodeActionKind.QuickFix
      ) {
        isPreferred = true
        edit = new vscode.WorkspaceEdit() {
          replace(
            uri = document.uri,
            range = range,
            newText = "hello"
          )
        }
        // optional command to run when the code action is selected
        // command = vscode
        //   .Command(
        //     title = "My Code Action",
        //     command = "myextension.myCodeAction.replaceWithHello"
        //   )
        //   .setTooltip("This is my code action")
      }

    // the code action for learn more
    val suggestedActionMore =
      new vscode.CodeAction(
        title = "learn more",
        kind = vscode.CodeActionKind.Empty
      ) {
        command = vscode
          .Command(
            title = "My Code Action",
            command = "myextension.myCodeAction.learnMore"
          )
          .setTooltip("This will show you more information")
      }

    js.Array(codeActionFix1, suggestedActionMore)
  }

}
