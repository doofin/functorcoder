import scala.sys.process.*
import org.scalajs.linker.interface.{ModuleKind, ModuleInitializer, ModuleSplitStyle}

val outdir = "out" // output directory for the extension
// open command in sbt
lazy val open = taskKey[Unit]("open vscode")
lazy val buildDebug = taskKey[Unit]("build debug")

lazy val root = project
  .in(file("."))
  .enablePlugins(
    ScalaJSPlugin,
    ScalaJSBundlerPlugin,
    ScalablyTypedConverterPlugin
  )
  // .configs(IntegrationTest)
  // .settings(Defaults.itSettings: _*)
  // .settings(inConfig(IntegrationTest)(ScalaJSPlugin.testConfigSettings): _*)
  .settings(
    moduleName := "vscextension",
    organization := "com.doofin",
    scalaVersion := "3.3.4",
    // warn unused imports and vars
    scalacOptions ++= Seq(
      "-Wunused:all",
      "-no-indent"
    ),
    // check if it is running in test
    // testOptions += Tests.Setup(_ => sys.props("testing") = "true"),
    Compile / fastOptJS / artifactPath := baseDirectory.value / "out" / "extension.js",
    Compile / fullOptJS / artifactPath := baseDirectory.value / "out" / "extension.js",
    resolvers ++= Seq(
      Resolver.jcenterRepo,
      "jitpack" at "https://jitpack.io",
      "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
    ),
    libraryDependencies ++= Seq(
      // "com.lihaoyi" %%% "utest" % "0.8.2" % "test",
      "org.latestbit" %%% "circe-tagged-adt-codec" % "0.11.0",
      "com.github.doofin.stdScala" %%% "stdscala" % "387b33df3a",

      // test dependencies
      "org.scalameta" %%% "munit" % "0.7.29" % Test
    ),
    Compile / npmDependencies ++=
      Seq(
        // vscode dependencies
        "@types/vscode" -> "1.96.0",
        // "@vscode/dts" -> "0.4.1", // it's just a utility to download sources
        "vscode-languageclient" -> "9.0.1", // working with manuallly created facade

        // other dependencies
        "@types/node" -> "16.11.7", // ts 3.7
        "@types/node-fetch" -> "2.5.12" // ts 3.7,compile error for scalablytyped

      ),
    /* ++ // check if it is running in test
        (if (sys.props.get("testing") != Some("true"))
           Seq(
             "@types/vscode" -> "1.96.0"
           )
         else Seq.empty), */
    stIgnore ++= List( // don't generate types with scalablytyped
    ),
    open := openVSCodeTask().dependsOn(Compile / fastOptJS).value,
    buildDebug := openVSCodeTask(openVscode = false).dependsOn(Compile / fastOptJS).value
  )

addCommandAlias("compile", ";fastOptJS")
addCommandAlias("dev", "~buildDebug")
addCommandAlias("fix", ";scalafixEnable;scalafixAll;")
// open, buildDebug are other commands added
/** prepare the extension and open vscode in extensionDevelopmentPath
  *
  * @param openVscode
  *   whether to open vscode or not. If false, it will just prepare the extension
  * @return
  */
def openVSCodeTask(openVscode: Boolean = true): Def.Initialize[Task[Unit]] =
  Def
    .task[Unit] {
      val baseDir = (ThisProject / baseDirectory).value
      val baseDirPath = baseDir.getCanonicalPath
      val logger = (ThisProject / streams).value.log

      printlnOrange("[compiling] extension")
      val _ = (Compile / fastOptJS).value
      // install deps to out dir
      printlnOrange("[copying] package.json to out dir")
      s"cp package.json ${outdir}/package.json" ! logger
      if (!(baseDir / outdir / "node_modules").exists) {
        printlnOrange("[installing] dependencies into out dir with npm")
        s"npm install --prefix ${outdir}" ! logger
      } else {
        printlnOrange("[skipping] dependencies installation")
      }
      // launch vscode
      if (openVscode) {
        val extensionPath = s"${baseDirPath}/${outdir}"
        printlnOrange(s"[opening] vscode" + s"with extensionDevelopmentPath=${extensionPath}")
        s"code --extensionDevelopmentPath=$extensionPath" ! logger
      }
      ()
    }

def printlnOrange(msg: Any): Unit = {
  println("\u001b[33m" + msg + "\u001b[0m")
}
