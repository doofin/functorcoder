package vscextension

import typings.vscode.mod as vscode
import typings.vscode.mod.TextEditor

import facade.vscodeUtils.*

object editorAPI {

  /** Shows various properties of the current document and editor
    *
    * like the language of the document, the project root, etc.
    */
  def showProps = {
    showMessageAndLog("document language: " + getLanguage())

    val projectRoot = vscode.workspace.rootPath.getOrElse("")
    showMessageAndLog("project root: " + projectRoot)

  }

  def getLanguage() = {
    vscode.window.activeTextEditor.toOption match {
      case None =>
        ""
      case Some(editor) =>
        editor.document.languageId
    }
  }

  def getCurrentDirectory() = {
    vscode.window.activeTextEditor.toOption.map(_.document.uri.path)
  }
}
