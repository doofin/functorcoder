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
import vscextension.editorAPI

/** Commands are actions that a user can invoke in the vscode extension with command palette (ctrl+shift+p).
  */
object Commands {
  type CommandT = Any => Any
  // all the commands here
  val cmdShowMenu =
    ("functorcoder.menu", quickPick.showMainMenu)
  val cmdAddDocs =
    ("functorcoder.addDocumentation", addDocumentation)

  val cmdCreateFiles =
    ("functorcoder.createFiles", createFilesCmd)

  // list of all commands to be registered
  def commandList(llm: llmAgent): Seq[(String, CommandT)] =
    Seq(
      (cmdShowMenu._1, cmdShowMenu._2(llm)),
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
    val currDir = editorAPI.getCurrentDirectory()

    currDir match {
      case None =>
        showMessageAndLog("no current directory, please open a file")
      case Some(value) =>
        // split the path
        val pathParts = value.split("/")
        // generate parent path for and 1 to 5 levels up
        val parentPaths =
          (1 to 5).map { i =>
            pathParts.take(pathParts.length - i).mkString("/")
          }

        quickPick.createQuickPick(
          title = "create files/folders",
          placeHolder = "select a parent folder",
          items = parentPaths.map { path =>
            (
              path,
              "",
              { () =>
                // create the files and folders according to the tree
                showMessageAndLog("creating files in: " + path)
                quickPick.createInputBox(
                  title = "Create files/folders under " + path,
                  placeHolder = "describe your project",
                  onInput = { input =>
                    val respFuture = llm.sendPrompt(functorcoder.llm.llmPrompt.CreateFiles(input))
                  respFuture.foreach { response =>
                    // parse the response to a tree of files and folders
                    val treeOpt = treeParse.parse(response)
                    val filesList = treeOpt.map(createFiles.tree2list).getOrElse(Seq()).mkString(", ")

                    quickPick.createQuickPick(
                      title = "Files and Folders",
                      placeHolder = "select to apply creating files and folders",
                      items = Seq(
                        (
                          s"create $filesList",
                          "",
                          { () =>
                            treeOpt match {
                              case scala.util.Success(tree) =>
                                createFiles.createFilesAndFolders(
                                  tree,
                                  path
                                )
                              case scala.util.Failure(exception) =>
                                showMessageAndLog(
                                  s"Failed to parse tree: ${treeOpt.toString}, exception: ${exception.getMessage}"
                                )
                            }
                            // create the files and folders according to the tree

                            // showMessageAndLog("files created!")
                          }
                        )
                      )
                    )
                  }
                  }
                )

              }
            )
          }
        )
    }

  }
}
