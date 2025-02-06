package vscextension.io


/* load configuration from directory like .vscode/config.json
 *
 *
 */
object config {
  def loadConfig(dir: String) = {
    println("loading config from " + dir)

    val strRead =
      fileIO.readFile(dir)

    strRead
  }
}
