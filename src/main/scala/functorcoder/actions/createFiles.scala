package functorcoder.actions

import com.doofin.stdScala.dataTypes.Tree.TreeNode
import functorcoder.algo.treeParse
import vscextension.facade.vscodeUtils.showMessageAndLog

/** create files and folders according to the prompt
  */
object createFiles {

  /** parse the prompt response to list of files and folders
    *
    * The prompt response is like: [(dir1/file1,"content1"), (dir2/file2,"content2")]
    *
    * it should return list like: List((dir1/file1, "content1"), (dir2/file2, "content2"))
    * @param promptResponse
    *   the response from the prompt
    */
  def parseFilesList(promptResponse: String, retry: Int = 3): Unit = {}

  /** parse the prompt response to tree of files and folders
    *
    * The prompt response is like: (root [(folder1 [(file1 file2) folder2]) folder3])
    *
    * assumes the prompt response is one of tree representation
    *
    * @param promptResponse
    *   the response from the prompt
    */
  def parseFilesTree(promptResponse: String, retry: Int = 3): Unit = {
    println("Creating files and folders")

    treeParse.parse(promptResponse) match {
      case scala.util.Success(tree) =>
        createFilesAndFolders(tree)
      case scala.util.Failure(exception) =>
        showMessageAndLog(s"Trying again with $retry retries left")
        if (retry > 0) {
          println(s"Retrying with retry=$retry")
          parseFilesTree(promptResponse, retry - 1)
        }
    }
  }

  /** create files and folders according to the tree
    *
    * @param tree
    *   the tree of files and folders
    */
  def createFilesAndFolders(tree: TreeNode[String]): Unit = {
    // recursively create files and folders
    showMessageAndLog(s"Files and folders tree: $tree")
    val TreeNode(root, children) = tree
    children.foreach { child =>
      createFilesAndFolders(child)
    }
  }
}
