package functorcoder.actions

import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.immutable
import scala.scalajs.js
import scala.concurrent.Future

import typings.vscode.mod as vscode

import vscextension.quickPick
import vscextension.facade.vscodeUtils.showMessageAndLog

import functorcoder.types.editorCtx.codeActionParam
import functorcoder.llm.llmMain.llmAgent
import functorcoder.algo.treeParse
import vscextension.statusBar

/** Commands are actions that a user can invoke in the vscode extension with command palette (ctrl+shift+p).
  */
object Commands {
  type CommandT = Any => Any
  // all the commands here
  val commandMenu =
    ("functorcoder.menu", quickPick.showQuickPick)
  val commandAddDocumentation =
    ("functorcoder.addDocumentation", addDocumentation)

  val commandCreateFiles =
    ("functorcoder.createFiles", createFilesCmd)

  // list of all commands to be registered
  def commandList(llm: llmAgent): Seq[(String, CommandT)] =
    Seq(
      commandMenu,
      commandAddDocumentation,
      (commandCreateFiles._1, commandCreateFiles._2(llm))
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

    statusBar.showSpininngStatusBarItem("functorcoder", llmResponse)

    llmResponse.foreach { response =>
      // showMessageAndLog("add doc: " + s"${param.documentUri}, ${param.range}, ${response}")
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

  }

  def createFilesCmd(llm: llmAgent)(arg: Any) = {
    val inputBoxOptions =
      vscode
        .InputBoxOptions()
        .setTitle("generate files and folders")
        .setPlaceHolder("type your description here")

    for {
      input <- vscode.window.showInputBox(inputBoxOptions).toFuture
      response <- llm.sendPrompt(
        functorcoder.llm.llmPrompt.CreateFiles(input match {
          case _: Unit   => "empty input!"
          case s: String => s
        })
      )
    } yield {
      showMessageAndLog("create files: " + s"${response}")
      val tree = treeParse.parse(response)
      quickPick.createQuickPick(
        title = "Files and Folders",
        items = Seq(
          (
            "files created!",
            tree.toString,
            { () =>
              showMessageAndLog("files created!")
            }
          )
        )
      )
    }
  }
}
