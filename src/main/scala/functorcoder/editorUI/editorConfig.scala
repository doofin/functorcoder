package functorcoder.editorUI

import functorcoder.llm.openaiReq

// https://code.visualstudio.com/api/references/contribution-points#contributes.configuration
object editorConfig {
  case class Config(
      openaiApiKey: String, //
      openaiUrl: String,
      maxTokens: Int,
      model: String = openaiReq.models.gpt4o
  )

}
