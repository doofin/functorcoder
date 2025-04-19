package functorcoder.types
import scala.scalajs.js

object editorTypes {

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

  /*    .Command(
        command = functorcoder.actions.Commands.cmdAddDocs._1, //
        title = "add documentation" //
      )
      .setArguments(
        js.Array(
          new codeActionParam(
            document.uri.toString(),
            range,
            llmResponse
          )
        )
      ) */
  case class commandData[Param](
      commandName: String,
      title: String,
      arguments: Param
  )
}
