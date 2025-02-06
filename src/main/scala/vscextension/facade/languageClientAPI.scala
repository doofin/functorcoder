package vscextension.facade

import scala.scalajs.js
import scala.scalajs.js.annotation._
import scala.scalajs.js.Promise

import typings.vscode.mod as vscode

// TODO
object languageClientAPI {
  @js.native
  @JSImport("vscode-languageclient/node", "LanguageClient")
  /** not compiled by scalably-typed, need manual implementation
    *
    * if Cannot find module 'vscode-languageclient/node', run npm install with the package.json in the root directory!
    *
    * @param id
    * @param name
    * @param serverOptions
    * @param clientOptions
    */
  class LanguageClient(
      id: String,
      name: String,
      serverOptions: js.Object,
      clientOptions: js.Object
  ) extends js.Object {

    /* the original example from
    https://github.com/microsoft/vscode-extension-samples/blob/main/lsp-sample/client/src/extension.ts
    import {
      LanguageClient,
      LanguageClientOptions,
      ServerOptions,
      TransportKind
    } from 'vscode-languageclient/node';

    let client: LanguageClient;

     */

    /** start the language client */
    def start(): Unit = js.native

    /** stop the language client */
    def stop(): Unit = js.native
  }

  @js.native
  trait ServerOptions extends js.Function0[Promise[MessageTransports]] {
    def apply(): Promise[MessageTransports] = js.native
  }

  @js.native
  trait MessageTransports extends js.Object

  @js.native
  trait LanguageClientOptions extends js.Object

  def test(): Unit = {
    // println("lspFacade.test")
    /* const serverModule = context.asAbsolutePath(
		path.join('server', 'out', 'server.js')
	);

	// If the extension is launched in debug mode then the debug server options are used
	// Otherwise the run options are used
	const serverOptions: ServerOptions = {
		run: { module: serverModule, transport: TransportKind.ipc },
		debug: {
			module: serverModule,
			transport: TransportKind.ipc,
		}
	};

	// Options to control the language client
	const clientOptions: LanguageClientOptions = {
		// Register the server for plain text documents
		documentSelector: [{ scheme: 'file', language: 'plaintext' }],
		synchronize: {
			// Notify the server about file changes to '.clientrc files contained in the workspace
			fileEvents: workspace.createFileSystemWatcher(
		}
	};

	// Create the language client and start the client.
	client = new LanguageClient(
		'languageServerExample',
		'Language Server Example',
		serverOptions,
		clientOptions
	);

	// Start the client. This will also launch the server
	client.start(); */

    val serverOptions =
      js.Dynamic.literal(
        run = js.Dynamic.literal(
          module = "serverModule",
          transport = "TransportKind.ipc"
        ),
        debug = js.Dynamic.literal(
          module = "serverModule",
          transport = "TransportKind.ipc"
        )
      )

    val clientOptions =
      js.Dynamic.literal(
        documentSelector = js.Array(
          js.Dynamic.literal(
            scheme = "file",
            language = "plaintext"
          )
        ),
        synchronize = js.Dynamic.literal( //
          fileEvents = vscode.workspace.createFileSystemWatcher("**/.clientrc")
        )
      )
    val client = new LanguageClient(
      "languageServerExample",
      "Language Server Example",
      serverOptions,
      clientOptions
    )

    client.start()
  }

}
