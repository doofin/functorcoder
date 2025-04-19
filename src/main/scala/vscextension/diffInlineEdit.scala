package vscextension

import functorcoder.editorUI.diffEdit.*

/** This object provides the diff edits for the vscode extension.
  *
  * currently, inline edit api is locked by vscode/microsoft as an insider feature.
  *
  * to overcome this, modify the product.json file to enable "proposed" api.
  *
  * https://github.com/microsoft/vscode/issues/190239
  * https://stackoverflow.com/questions/77202394/what-vs-code-api-can-i-use-to-create-an-in-editor-chat-box-like-in-github-copilo
  * https://stackoverflow.com/questions/76783624/vscode-extension-how-can-i-add-custom-ui-inside-the-editor
  *
  * https://github.com/microsoft/vscode/blob/main/src/vscode-dts/vscode.proposed.inlineEdit.d.ts
  *
  * related: source for vscode
  * https://github.com/microsoft/vscode/blob/main/src/vs/workbench/services/extensions/browser/extensionService.ts#L226
  *
  * the product.json location has changed several times. /opt/visual-studio-code/resources/app/product.json
  *
  * use `strace -f code 2>&1 | grep product` to find the location of the product.json
  *
  * for more info: https://github.com/VSCodium/vscodium/blob/master/docs/index.md
  */
object diffInlineEdit {

  /** create a diff edit in the editor
    *
    * @param diffResult
    *   the diff result
    */
  def createDiffEdit(diffResult: DiffResult): Unit = {}
}
