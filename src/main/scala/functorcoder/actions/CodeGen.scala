package functorcoder.actions

import functorcoder.llm.llmMain.llmAgent
import functorcoder.llm.llmPrompt
import scala.concurrent.Future
import vscextension.editorAPI

object CodeGen {

  /** Generates a code completion suggestion by sending a prompt to a language model.
    *
    * @param codeBefore
    *   The code snippet preceding the hole where completion is required.
    * @param codeAfter
    *   The code snippet following the hole where completion is required.
    * @param llm
    *   The language model agent used to generate the completion.
    * @return
    *   A `Future` containing the generated code completion as a `String`.
    */
  def getCompletion(
      codeBefore: String, // code before the hole
      codeAfter: String, // code after the hole
      llm: llmAgent
  ): Future[String] = {

    val prompt = llmPrompt
      .Completion(codeWithHole = s"$codeBefore${llmPrompt.promptText.hole}$codeAfter")

    // assistantMessage: String = promptText.prompt1
    llm.sendPrompt(prompt)
  }

  def getDocumentation(
      selectedCode: String,
      llm: llmAgent
  ) = {
    val language = editorAPI.getLanguage()
    val llmResponse =
      llm.sendPrompt(
        llmPrompt.Modification(
          code = selectedCode, //
          taskRequirement = llmPrompt.generateDocs(language)
        )
      )

    val commandName = functorcoder.actions.Commands.cmdAddDocs._1

    (llmResponse, commandName)
  }

}
