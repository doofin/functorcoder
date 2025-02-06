package vscextension.io

import typings.node.fsMod
import typings.node.fsMod.PathOrFileDescriptor
import typings.node.bufferMod.global.BufferEncoding

import vscextension.facade.vscodeUtils

/** IO utilities for input/output operations, like creating files/folders
  *
  * This object contains utilities for IO operations.
  */
object fileIO {
  def createFile(projectRoot: String) = {
    // create folder target/test and file target/test/helo
    // note: it uses absolute path!
    val testFolder = "target"
    vscodeUtils.consoleLog("creating file")

    fsMod.mkdirSync(s"$projectRoot/$testFolder")
    fsMod.writeFileSync(s"$projectRoot/$testFolder/helo", "Hello, World!")
  }

  def readFile(file: String): String = {
    fsMod.readFileSync(
      file.asInstanceOf[PathOrFileDescriptor], //
      BufferEncoding.utf8
    )
  }
}
