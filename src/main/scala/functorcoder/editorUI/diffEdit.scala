package functorcoder.editorUI

import scala.concurrent.ExecutionContext.Implicits.global

import functorcoder.llm.llmMain
import functorcoder.llm.llmPrompt

/** This abstract the editor behavior for inline editing
  *
  * Scenario: user wants to edit a selected snippet of code in the editor, the coding assistant will provide a modified
  * version of the snippet with the changes highlighted, and the user can choose to accept or reject the changes.
  */
object diffEdit {

  case class CursorPosition(
      line: Int,
      character: Int
  )

  case class DiffRequest(
      oldText: String,
      cursorPosition: CursorPosition,
      task: String // the task to perform
  )

  /** The result of the diff operation
    *
    * @param oldText
    *   the old text
    * @param newText
    *   the new text
    * @param cursorPosition
    *   the cursor position
    * @param difference
    *   the difference between the old and new text
    */
  case class DiffResult(
      oldText: String,
      newText: String,
      cursorPosition: CursorPosition
      // difference: Seq[String]
  )

  /** action to modify the code, like adding new code
    *
    * @param llmAgent
    *   the agent to perform the diff operation
    * @param diffReq
    *   the diff request
    * @return
    *   the result of the diff operation
    */
  def diff(llmAgent: llmMain.llmAgent, diffReq: DiffRequest) = {
    val prompt = llmPrompt.Modification(
      code = diffReq.oldText,
      taskRequirement = ""
    )

    val llmResponse = llmAgent.sendPrompt(prompt)

    llmResponse.map(txt =>
      DiffResult(
        oldText = diffReq.oldText,
        newText = txt,
        cursorPosition = diffReq.cursorPosition
      )
    )
  }
}
