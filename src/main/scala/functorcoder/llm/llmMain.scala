package functorcoder.llm

import openaiReq.*
import typings.nodeFetch.mod as nodeFetch

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js

import vscextension.facade.vscodeUtils.*

import scala.scalajs.js.Thenable.Implicits.*
import scala.concurrent.Future

import functorcoder.editorUI.editorConfig

/** large language model (LLM) AI main
  *
  * use node-fetch for network requests
  */
object llmMain {

  /** generate a completion prompt
    *
    * change the model here if needed
    *
    * @param completionPrompt
    *   completion prompt object
    * @return
    */
  def getCompletionPrompt(completionPrompt: llmPrompt.Completion) = {
    openaiReq
      .OpenAiRequest(
        List(
          openaiReq.Message(roles.user, completionPrompt.codeWithHole),
          openaiReq.Message(roles.system, completionPrompt.assistantMessage)
        ),
        openaiReq.models.gpt4o
      )
      .toJson
  }

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
    def getCodeCompletion(holeToCode: String => String) = {
      val requestStr = getCompletionPrompt(
        llmPrompt
          .Completion(holeToCode("{{FILL_HERE}}"))
      )

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
