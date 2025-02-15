package functorcoder.types
import scala.scalajs.js

object editorCtx {

  /** the context of the editor
    *
    * @param language
    *   the programming language of the file
    */
  case class EditorContext(
      language: String
  )

  class codeActionParam[T](
      val documentUri: String, //
      val range: typings.vscode.mod.Selection,
      val param: T
  ) extends js.Object
}
