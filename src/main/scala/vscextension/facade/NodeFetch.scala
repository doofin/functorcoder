package vscextension.facade

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import org.scalajs.dom.Headers

object NodeFetch {

  /** node-fetch is a http client for node.js
    *
    * https://github.com/node-fetch/node-fetch
    *
    * typescript definition: https://github.com/node-fetch/node-fetch/blob/main/%40types/index.d.ts
    *
    * scala-js facade from from https://github.com/scalajs-io/node-fetch/
    */
  @deprecated("use scalablyTyped/NodeFetch instead")
  @js.native
  @JSImport("node-fetch", JSImport.Default)
  object NodeFetch extends js.Object {

    /** Asynchronously executes an HTTP request
      * @param url
      *   should be an absolute url, eg http://example.com/
      * @param options
      *   the optional settings
      * @return
      *   the promise of a [[FetchResponse response]]
      */
    def apply(url: String, options: RequestOptions = js.native): js.Promise[FetchResponse] = js.native

  }

  @js.native
  trait FetchResponse extends js.Object {

    /** node js readable stream
      */
    def body: Readable = js.native

    def ok: Boolean = js.native

    def status: Int = js.native

    def statusText: String = js.native

    def headers: Headers = js.native

    def json(): js.Promise[js.Any] = js.native

    def text(): js.Promise[String] = js.native
  }

  /** Request Options
    * @param agent
    *   http.Agent instance, allows custom proxy, certificate etc.
    * @param body
    *   request body. can be a string, buffer, readable stream
    * @param compress
    *   support gzip/deflate content encoding. false to disable
    * @param follow
    *   maximum redirect count. 0 to not follow redirect
    * @param headers
    *   request header. format {a:'1'} or {b:['1','2','3']}
    * @param method
    *   the HTTP method (e.g. "GET")
    * @param redirect
    *   set to `manual` to extract redirect headers, `error` to reject redirect (e.g. "follow")
    * @param size
    *   maximum response body size in bytes. 0 to disable
    * @param timeout
    *   maximum redirect count. 0 to not follow redirect
    */

  class RequestOptions(
      val agent: js.UndefOr[String] = js.undefined,
      val body: js.Any = js.undefined,
      val compress: js.UndefOr[Boolean] = js.undefined,
      val follow: js.UndefOr[Int] = js.undefined,
      val headers: js.Any = js.undefined,
      val method: js.UndefOr[String] = js.undefined,
      val redirect: js.UndefOr[String] = js.undefined,
      val size: js.UndefOr[Int] = js.undefined,
      val timeout: js.UndefOr[Int] = js.undefined
  ) extends js.Object
}
