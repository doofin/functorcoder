package functorcoder.actions

import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.immutable
import scala.scalajs.js
import scala.concurrent.Future

import typings.vscode.mod as vscode

import vscextension.quickPick
import vscextension.facade.vscodeUtils.showMessageAndLog

import functorcoder.types.editorCtx.codeActionParam

/** Commands are actions that a user can invoke in the vscode extension with command palette (ctrl+shift+p).
  */
object Commands {
  type CommandT = Any => Any
  // all the commands here
  val commandMenu =
    ("functorcoder.menu", quickPick.showQuickPick)
  val commandAddDocumentation =
    ("functorcoder.addDocumentation", addDocumentation)

  // list of all commands to be registered
  val commandList: Seq[(String, CommandT)] =
    Seq(
      commandMenu,
      commandAddDocumentation
    )

    // the main menu items
  val mainMenuItems: Seq[(String, () => Unit)] = Seq(
    "create files" -> { () => println("create files") }
  )

  // individual command handlers
  def addDocumentation(arg: Any) = {
    val param =
      arg.asInstanceOf[codeActionParam[Future[String]]]
    val llmResponse = param.param
    llmResponse.foreach { response =>
      showMessageAndLog("add doc: " + s"${param.documentUri}, ${param.range}, ${response}")
      // apply the changes to the document
      vscode.window.activeTextEditor.toOption match {
        case None =>
          showMessageAndLog("no active editor!")
        case Some(ed) =>
          ed.insertSnippet(
            new vscode.SnippetString(response + "\n"), //
            param.range.start // insert at the start of the selection
          )
      }

    }

    // showMessageAndLog("add documentation: " + s"${dyn.uri}, ${dyn.range}, ${dyn.llmResponse}")
  }
}
