package functorcoder.editorUI

import typings.vscode.mod as vscode
// https://code.visualstudio.com/api/references/contribution-points#contributes.configuration
object editorConfig {
  case class Config(openaiApiKey: String, openaiUrl: String)

  /** read the configuration from the vscode settings.json
    *
    * @return
    *   the configuration object
    */
  def readConfig() = {
    val config = vscode.workspace.getConfiguration("functorcoder")
    val openaiApiKey =
      config.getStringOrEmpty("openaiApiKey")

    val openaiUrl =
      config.getStringOrEmpty("openaiUrl", "https://api.openai.com/v1/chat/completions")

    Config(openaiApiKey, openaiUrl)
  }

  extension (config: vscode.WorkspaceConfiguration) {

    /** get a string from the configuration or return an empty string if default is not provided
      *
      * @param str
      *   the string to get
      * @param default
      *   the default value
      * @return
      *   the string or an empty string
      */
    def getStringOrEmpty(str: String, default: String = "") = {
      config.get[String](str).toOption.getOrElse(default)
    }
  }
}
