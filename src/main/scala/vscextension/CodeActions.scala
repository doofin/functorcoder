package vscextension
import typings.vscode.mod as vscode
import scala.scalajs.js
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.JSConverters.JSRichFutureNonThenable

import facade.vscodeUtils.*
import functorcoder.llm.llmMain.llmAgent
import functorcoder.llm.llmPrompt
import cats.syntax.show
import scala.concurrent.duration.Duration
import scala.concurrent.Await
import scala.concurrent.Future

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
          val llmResponse =
            llm.sendPrompt(
              llmPrompt.Modification(
                code = selectedCode, //
                taskRequirement = "add documentation"
              )
            )

          val editor = new vscode.WorkspaceEdit() {
            // linking issue from compiler
            // llmResponse.wait()
            // val response = Await.result(llmResponse, Duration.Inf)
            // // llmResponse.toJSPromise.wait()
            // showMessageAndLog("llm response: " + response)
            // insert(
            //   uri = document.uri,
            //   position = range.start,
            //   newText = response
            // )
          }

          llmResponse.foreach { response =>
            showMessageAndLog("llm response: " + response)
            // does not work since it is not in the same thread?
            editor.insert(
              uri = document.uri,
              position = range.start,
              newText = response
            )
          }

          val fix1 =
            new vscode.CodeAction(
              title = "generate documentation",
              kind = vscode.CodeActionKind.QuickFix
            ) {
              isPreferred = true // show it first
              // should invoke a command to perform the action
              import functorcoder.types.editorCtx.*
              val args: codeActionParam[Future[String]] = new codeActionParam(
                document.uri.toString(),
                range,
                llmResponse
              )

              command = vscode
                .Command(
                  command = functorcoder.actions.Commands.commandAddDocumentation, //
                  title = "add documentation" //
                )
                .setArguments(js.Array(args))

              // edit = editor
              // optional command to run when the code action is selected
              // command = ..
            }
            // can return array or promise of array

          js.Array(fix1)

          // the code action for learn more
        }

        // override def provideCodeActions
        def provideCodeActions(
            document: vscode.TextDocument,
            range: vscode.Selection,
            context: vscode.CodeActionContext,
            token: vscode.CancellationToken
        ): vscode.ProviderResult[js.Array[vscode.CodeAction]] = {

          // check who triggers the code action, since vscode may trigger it automatically
          val res = context.triggerKind match {
            case vscode.CodeActionTriggerKind.Invoke =>
              // triggered by user

              showMessageAndLog("selected code: " + document.getText(range))
              createCodeAction(document, range, context)

            case _ =>
              // vscode triggered it automatically, just return an empty array
              js.Array()

          }

          res.asInstanceOf[vscode.ProviderResult[js.Array[vscode.CodeAction]]]

        }
        // cast the object to the required type
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
    showMessageAndLog("registered code actions")
  }

}
