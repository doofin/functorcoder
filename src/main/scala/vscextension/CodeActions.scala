package vscextension

import scala.scalajs.js
import scala.concurrent.Future

import typings.vscode.mod as vscode

import facade.vscodeUtils.*
import functorcoder.llm.llmMain.llmAgent
import functorcoder.types.editorTypes.*
import functorcoder.actions.CodeGen

/** Code actions are commands provided at the cursor in the editor, so users can
  *
  * quickly fix issues or refactor code, etc.
  *
  * https://github.com/microsoft/vscode-extension-samples/blob/main/code-actions-sample/README.md
  */
object CodeActions {

  def registerCodeActions(context: vscode.ExtensionContext, llm: llmAgent) = {

    // https://scalablytyped.org/docs/encoding#jsnative
    // we need to manually create the js object and cast it
    val mActionProvider =
      new js.Object {
        def createCodeAction(
            document: vscode.TextDocument,
            range: vscode.Selection,
            context: vscode.CodeActionContext
        ) = {
          val selectedCode = document.getText(range)

          val addDocsItem =
            new vscode.CodeAction(
              title = "add documentation for selected code",
              kind = vscode.CodeActionKind.QuickFix
            ) {
              isPreferred = true // show it first
              val language = editorAPI.getLanguage()
              val (llmResponse, commandName) =
                CodeGen.getDocumentation(
                  selectedCode,
                  llm
                )

              statusBar.showSpininngStatusBarItem(s"functorcoder($language)", llmResponse)

              // there are no onSelect events for code actions
              // so we need to create a command and set it here
              // edit = new vscode.WorkspaceEdit() {
              //   showMessageAndLog("creating edit") // triggered immediately
              // }
              // invoke command

              command = vscode
                .Command(
                  command = commandName, //
                  title = "add documentation" //
                )
                .setArguments(
                  js.Array(
                    new codeActionParam(
                      document.uri.toString(),
                      range,
                      llmResponse
                    )
                  )
                )

            }
            // can return array or promise of array

          js.Array(addDocsItem)
        }

        def provideCodeActions(
            document: vscode.TextDocument,
            range: vscode.Selection,
            context: vscode.CodeActionContext,
            token: vscode.CancellationToken
        ): vscode.ProviderResult[js.Array[vscode.CodeAction]] = {

          // check who triggers the code action, since vscode may trigger it automatically
          context.triggerKind match {
            case vscode.CodeActionTriggerKind.Invoke =>
              // triggered by user

              // showMessageAndLog("selected code: " + document.getText(range))
              createCodeAction(document, range, context)

            case _ =>
              // vscode triggered it automatically, just return an empty array
              js.Array()

          }
        }
      }.asInstanceOf[vscode.CodeActionProvider[vscode.CodeAction]]

    val registration: vscode.Disposable =
      vscode.languages.registerCodeActionsProvider(
        selector = "*",
        provider = mActionProvider,
        metadata = vscode
          .CodeActionProviderMetadata()
          .setProvidedCodeActionKinds(
            js.Array(vscode.CodeActionKind.QuickFix)
          )
      )

    context.pushDisposable(registration)
  }

}
