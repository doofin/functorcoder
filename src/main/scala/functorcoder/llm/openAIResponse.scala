package functorcoder.llm

import io.circe._
import io.circe.generic.semiauto._
import io.circe.parser.*

/** response from openAI API
  *
  * https://platform.openai.com/docs/api-reference/making-requests
  */
object openAIResponse {

  def decodeOpenAIResponse(json: String): Either[Error, OpenAiResponse] = {
    val res =
      decode[OpenAiResponse](escapeJsonString(json))

    res match {
      case x @ Left(value)  => x
      case x @ Right(value) => x
    }
  }

  def escapeJsonString(str: String): String = {
    // replace the escape characters with <newline> ,etc.
    str
      .replace("\n", "<newline>")
      .replace("\t", "<tab>")
      .replace("\r", "<return>")
  }

  def reverseEscapeJsonString(str: String): String = {
    str
      .replace("<newline>", "\n")
      .replace("<tab>", "\t")
      .replace("<return>", "\r")
  }

  case class OpenAiResponse(
      id: String,
      `object`: String,
      created: Long,
      model: String,
      usage: Usage,
      choices: List[Choice]
  )

  case class Choice(
      message: Message,
      logprobs: Option[String],
      finish_reason: String,
      index: Int
  )

  case class Message(
      role: String, // the role
      content: String // the content from llm
  )

  case class CompletionTokensDetails(
      reasoning_tokens: Int,
      accepted_prediction_tokens: Int,
      rejected_prediction_tokens: Int
  )

  case class Usage(
      prompt_tokens: Int,
      completion_tokens: Int,
      total_tokens: Int,
      completion_tokens_details: CompletionTokensDetails
  )

//  encode and decode

  object Choice {
    implicit val encoder: Encoder[Choice] = deriveEncoder[Choice]
    implicit val decoder: Decoder[Choice] = deriveDecoder[Choice]
  }

  object OpenAiResponse {
    implicit val encoder: Encoder[OpenAiResponse] = deriveEncoder[OpenAiResponse]
    implicit val decoder: Decoder[OpenAiResponse] = deriveDecoder[OpenAiResponse]
  }

}
