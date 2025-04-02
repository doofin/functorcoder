package vscextension

import scala.scalajs.js
import scala.concurrent.Future

import typings.vscode.mod as vscode

import facade.vscodeUtils.*
import functorcoder.llm.llmMain.llmAgent
import functorcoder.llm.llmPrompt
import functorcoder.types.editorCtx.*

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
          val language = documentProps.getLanguage()
          val llmResponse =
            llm.sendPrompt(
              llmPrompt.Modification(
                code = selectedCode, //
                taskRequirement = llmPrompt.generateDocs(language)
              )
            )

            // show the spinner when waiting
          statusBar.showSpininngStatusBarItem(s"functorcoder($language)", llmResponse)
          val fix1 =
            new vscode.CodeAction(
              title = "add documentation for selected code",
              kind = vscode.CodeActionKind.QuickFix
            ) {
              isPreferred = true // show it first
              val args: codeActionParam[Future[String]] = new codeActionParam(
                document.uri.toString(),
                range,
                llmResponse
              )
              // invoke command
              command = vscode
                .Command(
                  command = functorcoder.actions.Commands.cmdAddDocs._1, //
                  title = "add documentation" //
                )
                .setArguments(js.Array(args))

            }
            // can return array or promise of array

          js.Array(fix1)
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
