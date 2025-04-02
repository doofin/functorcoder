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
  val cmdShowMenu =
    ("functorcoder.menu", quickPick.showQuickPick)
  val cmdAddDocs =
    ("functorcoder.addDocumentation", addDocumentation)

  val cmdCreateFiles =
    ("functorcoder.createFiles", createFilesCmd)

  // list of all commands to be registered
  def commandList(llm: llmAgent): Seq[(String, CommandT)] =
    Seq(
      cmdShowMenu,
      cmdAddDocs,
      (cmdCreateFiles._1, cmdCreateFiles._2(llm))
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
    quickPick.createInputBox(
      title = "Create files/folders description",
      placeHolder = "describe your project",
      onInput = { input =>
        llm
          .sendPrompt(
            functorcoder.llm.llmPrompt.CreateFiles(input)
          )
          .foreach { response =>
            showMessageAndLog("create files: " + s"${response}")
            val tree = treeParse.parse(response)
            quickPick.createQuickPick(
              title = "Files and Folders",
              placeHolder = "select to apply creation",
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
    )

    /* for {
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
    } */
  }
}
