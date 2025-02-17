package vscextension
import typings.vscode.mod as vscode

import vscextension.facade.vscodeUtils.*

import functorcoder.actions.Commands
object statusBar {

  def createStatusBarItem(context: vscode.ExtensionContext) = {
    val statusBarItem =
      vscode.window.createStatusBarItem(vscode.StatusBarAlignment.Right)

    val name = "functor"
    statusBarItem.text = name
    statusBarItem.name = name
    statusBarItem.command = Commands.commandMenu._1
    statusBarItem.show()

    context.pushDisposable(statusBarItem.asInstanceOf[vscode.Disposable])
    statusBarItem
  }
}
