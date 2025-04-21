package vscextension
import scala.scalajs.js
import typings.vscode.mod as vscode

import vscextension.facade.vscodeUtils.*
import functorcoder.actions.Commands
import scala.concurrent.Future
import scala.scalajs.js.JSConverters.JSRichFutureNonThenable
import scala.concurrent.ExecutionContext.Implicits.global
import functorcoder.llm.llmMain.llmAgent

object statusBar {

  def createStatusBarItem(context: vscode.ExtensionContext, llm: llmAgent) = {
    val statusBarItem =
      vscode.window.createStatusBarItem(vscode.StatusBarAlignment.Right)

    val name = "functor"
    statusBarItem.text = name
    statusBarItem.name = name
    statusBarItem.command = Commands.cmdShowMenu._1
    statusBarItem.show()

    context.pushDisposable(statusBarItem.asInstanceOf[vscode.Disposable])
    statusBarItem
  }

  /** Show a spinning status bar item while loading
    * @param text
    *   the text to show
    * @param promise
    *   the promise to wait for
    */
  def showSpininngStatusBarItem(text: String, promise: js.Promise[Any]): vscode.Disposable = {
    // show a spinner while loading
    vscode.window.setStatusBarMessage(
      "$(sync~spin)" + text,
      hideWhenDone = promise.asInstanceOf[typings.std.PromiseLike[Any]]
    )
  }

  def showSpininngStatusBarItem(text: String, future: Future[Any]): vscode.Disposable = {
    showSpininngStatusBarItem(text, future.toJSPromise)
  }
}
