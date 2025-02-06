package vscextension.facade
import typings.vscode.mod as vscode
import scala.scalajs.js
import typings.vscode.anon.Dispose

object vscodeUtils {
  extension (context: vscode.ExtensionContext) {
    def pushDisposable(disposable: vscode.Disposable): Unit = {
      context.subscriptions.push(disposable.asInstanceOf[Dispose])
    }
  }

  def showMessage(message: String): Unit = {
    vscode.window.showInformationMessage(message)
  }

  def consoleLog(message: String): Unit = {
    js.Dynamic.global.console.log(message)
  }

  def showMessageAndLog(message: String): Unit = {
    showMessage(message)
    consoleLog(message)
  }
}
