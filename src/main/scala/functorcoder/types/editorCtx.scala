package functorcoder.types

object editorCtx {

  /** the context of the editor
    *
    * @param language
    *   the programming language of the file
    */
  case class EditorContext(
      language: String
  )
}
