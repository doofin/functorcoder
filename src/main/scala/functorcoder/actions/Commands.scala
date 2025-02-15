package functorcoder.actions
import scala.concurrent.ExecutionContext.Implicits.global

import typings.vscode.mod as vscode

import scala.collection.immutable
import scala.scalajs.js

import vscextension.quickPick
import vscextension.facade.vscodeUtils.showMessageAndLog
import scala.concurrent.Future
import functorcoder.types.editorCtx.codeActionParam

/** Commands are actions that a user can invoke in the vscode extension with command palette (ctrl+shift+p).
  */
object Commands {
  type CommandT = Any => Unit
  // all the commands here
  val commandMenu = "functorcoder.menu"
  val commandAddDocumentation = "functorcoder.addDocumentation"

  val commandList: Seq[(String, CommandT)] =
    Seq(
      (commandMenu, quickPick.showQuickPick),
      (commandAddDocumentation, addDocumentation)
    )

    // the main menu items
  val mainMenuItems: Seq[(String, () => Unit)] = Seq(
    "create files" -> { () => println("create files") }
  )

  def addDocumentation(arg: Any) = {
    val param =
      arg.asInstanceOf[codeActionParam[Future[String]]]
    val llmResponse = param.param
    llmResponse.foreach { response =>
      showMessageAndLog("add doc: " + s"${param.documentUri}, ${param.range}, ${response}")
    }

    // showMessageAndLog("add documentation: " + s"${dyn.uri}, ${dyn.range}, ${dyn.llmResponse}")
  }
}
