package vscextension

/** This object provides the diff edits for the vscode extension.
  *
  * currently, inline edit api is locked by vscode/microsoft as an insider feature.
  *
  * you need modify the product.json file to enable "proposed" api.
  *
  * https://github.com/microsoft/vscode/issues/190239
  * https://stackoverflow.com/questions/77202394/what-vs-code-api-can-i-use-to-create-an-in-editor-chat-box-like-in-github-copilo
  * https://stackoverflow.com/questions/76783624/vscode-extension-how-can-i-add-custom-ui-inside-the-editor
  *
  * https://github.com/microsoft/vscode/blob/main/src/vscode-dts/vscode.proposed.inlineEdit.d.ts
  */
object diffEdit {
//   new MappedEditsProvider
}
