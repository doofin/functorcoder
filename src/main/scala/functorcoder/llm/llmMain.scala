package functorcoder.llm

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js

import scala.scalajs.js.Thenable.Implicits.*
import scala.concurrent.Future

import typings.nodeFetch.mod as nodeFetch

import vscextension.facade.vscodeUtils.*
import openaiReq.*
import functorcoder.editorUI.editorConfig

/** large language model (LLM) main entry
  */
object llmMain {

  /** prompt data to string
    *
    * change the model here if needed
    *
    * @param completionPrompt
    *   completion prompt object
    * @return
    */
  def prompt2str(editorCfg: editorConfig.Config, inputPrompt: llmPrompt.Prompt) = {
    // showMessageAndLog(s"prompt: ${inputPrompt}")

    val openAiRequest = openaiReq
      .OpenAiRequest(
        List(
          openaiReq.Message(roles.user, inputPrompt.generatePrompt),
          openaiReq.Message(roles.system, inputPrompt.getAssistantMessage)
        ),
        editorCfg.model,
        max_tokens = Some(editorCfg.maxTokens)
      )

    // showMessageAndLog(s"openai request: ${openAiRequest}")
    openAiRequest.toJson
  }

  /** llm agent to send request to openai api
    *
    * @param editorCfg
    *   the editor configuration
    */
  case class llmAgent(editorCfg: editorConfig.Config) {

    val url = editorCfg.openaiUrl
    val apiKey = editorCfg.openaiApiKey

    /** get code completion from openai asynchrnously
      *
      * @param holeToCode
      *   a function that takes a hole name and returns the code to fill the hole
      *
      * so we will call this function with the hole "{{FILL_HERE}}" you insert it in the code
      */
    def sendPrompt(input: llmPrompt.Prompt) = {

      val requestStr = prompt2str(editorCfg, input)

      val requestOptions = getRequestOptions(requestStr)

      val responseFuture =
        nodeFetch.default(url, requestOptions)

      getResponseText(responseFuture)
    }

    private def getRequestOptions(requestStr: String) = {
      new nodeFetch.RequestInit {
        method = "POST"
        headers = new nodeFetch.Headers {
          append("Content-Type", "application/json")
          append("Authorization", s"Bearer $apiKey")
        }
        body = requestStr
      }
    }

    /** get the response text from ai api, only the content of the first choice
      *
      * it parses the response json and returns the first choice
      *
      * @param responseFuture
      *   the response future
      * @return
      *   the response text
      */
    private def getResponseText(responseFuture: Future[nodeFetch.Response]) = {
      for {
        res <- responseFuture
        body <- res
          .json()
          .toFuture
          .asInstanceOf[Future[js.Object]]
          .map(x => js.JSON.stringify(x))
      } yield {
        // the body of the response
        // showMessageAndLog(s"openai response: $body")
        val decodedResponse =
          openAIResponse.decodeOpenAIResponse(body)
        decodedResponse match {
          case Left(err) =>
            // return an empty string if failed
            showMessageAndLog(s"error parsing openai response: $err")
            ""
          case Right(resp) =>
            // return the first choice
            resp.choices.headOption match {
              case Some(choice) => choice.message.content
              case None         => ""
            }
        }
      }
    }
  }

}
