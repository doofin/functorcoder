package functorcoder.editorUI

/** This abstract the editor behavior for inline editing
  *
  * Scenario: A new version of the file is shown to the user in the editor with the changes highlighted,
  *
  * and the user can choose to accept or reject the changes.
  */
object diffEdit {

  case class CursorPosition(
      line: Int,
      character: Int
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
      cursorPosition: CursorPosition,
      difference: Seq[String]
  )
}
