package functorcoder.editorUI

// https://code.visualstudio.com/api/references/contribution-points#contributes.configuration
object editorConfig {
  case class Config(
      openaiApiKey: String, //
      openaiUrl: String,
      maxTokens: Int,
      model: String = "gpt-4o-mini"
  )

}
