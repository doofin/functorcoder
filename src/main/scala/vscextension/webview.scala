package vscextension
import scala.scalajs.js
import typings.vscode.mod as vscode
/* an example of a webview in vscode which can show arbitrary html content
 */
object webview {
  def showWebviewPanel(): Unit = {
    val panel = vscode.window.createWebviewPanel(
      viewType = "webview",
      title = "Webview",
      showOptions = vscode.ViewColumn.One
    )

    panel.webview.html = """
            <html>
            <body>
            <h1>Hello, Webview!</h1>
            </body>
            </html>
        """
  }
}
