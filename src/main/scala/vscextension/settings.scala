package vscextension

import typings.vscode.mod as vscode
import functorcoder.editorUI.editorConfig.Config

object settings {

  /** read the configuration from the vscode settings.json
    *
    * https://code.visualstudio.com/api/references/contribution-points#contributes.configuration
    */
  def readConfig() = {
    val config = vscode.workspace.getConfiguration("functorcoder")
    val openaiApiKey =
      config.getStringOrEmpty("openaiApiKey")

    val openaiUrl =
      config.getStringOrEmpty(key = "openaiUrl", default = "https://api.openai.com/v1/chat/completions")

    val maxTokens = config.get[Int]("maxTokens").getOrElse(1000)
    val model = config.getStringOrEmpty("model", default = "gpt-4o-mini")
    Config(openaiApiKey, openaiUrl, maxTokens, model)
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
    def getStringOrEmpty(key: String, default: String = "") = {
      // if the key is found but the value is empty, return the default

      config.get[String](key).toOption match {
        case None        => default
        case Some(value) => if (value.isEmpty) default else value
      }
    }
  }
}
