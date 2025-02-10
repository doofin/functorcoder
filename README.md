# functorcoder
Open source AI coding assistant "**functorcoder**" is a AI coding assistant utilizing LLM (Large Language Model) with algebraic and modular design.

features:
- code generation: completion, documentation
- code modification: refactoring, optimization, bug fixing
- code analysis: code understanding, code review, code quality

we think in mathematics, algebra and functional programming.

    Input = {Query, CodeSnippet, Spec}: The set of all possible input types (queries, code snippets, or requirements/specifications).

    Output = {Code, Explanation, Transformation, DebugSuggestion}: The set of all possible outputs.

The types and objects for Input:
- code snippet or code file: a piece of code
- code context: a code snippet with its surrounding code
- query: natural language query 
- specification: natural language specification

The Output:
- code snippet or code file: a piece of code, including completion, refactoring, optimization, bug fixing
- explanation: a natural language explanation
- transformation: the transformation of the input code
- suggestion: a suggestion for debugging or improvement or refactoring


## Project Structure
package name: com.functorcoder

project file structure:
```bash
/functorcoder
├── /src/main/scala/functorcoder
│   ├── /core
│   │   ├── AIEngine.scala         # Core engine with main orchestration logic
│   │   └── Utils.scala            # Helper utilities
│   ├── /llm
│   │   ├── LLM.scala              # Large Language Model (LLM) integration
│   ├── /actions
│   │   ├── CodeCompletion.scala   # Code completion module
│   │   ├── Refactor.scala         # Refactor code module
│   │   └── Debug.scala            # Debugging module
│   ├── /types
│   │   ├── InputTypes.scala     # Types for code, context, and user actions
│   │   └── OutputTypes.scala      # Types for output (formatted code, suggestions)
│   ├── /editorUI
│   │   ├── EditorIntegration.scala# Integration with the editor (e.g., VSCode)
│   └── /tests
│       ├── CoreTests.scala         # Unit tests for core modules
│       ├── ActionTests.scala       # Unit tests for actions like code completion
│       └── EditorTests.scala      # Tests for editor integration
└── /docs
    ├── README.md                 # Project overview and setup instructions
    ├── ARCHITECTURE.md           # Architecture details and design decisions
    └── API.md                    # API documentation for integration
```

The bash commands to create above structure(folders and files) are:
```bash
mkdir -p src/main/scala/functorcoder/core
mkdir -p src/main/scala/functorcoder/llm
mkdir -p src/main/scala/functorcoder/actions
mkdir -p src/main/scala/functorcoder/types
mkdir -p src/main/scala/functorcoder/editorUI
mkdir -p src/main/scala/functorcoder/tests
mkdir -p docs

touch src/main/scala/functorcoder/core/AIEngine.scala
touch src/main/scala/functorcoder/core/Utils.scala
touch src/main/scala/functorcoder/llm/LLM.scala
touch src/main/scala/functorcoder/actions/CodeCompletion.scala
touch src/main/scala/functorcoder/actions/Refactor.scala
touch src/main/scala/functorcoder/actions/Debug.scala

touch src/main/scala/functorcoder/types/InputTypes.scala
```

### Setup
Requirements:
 - [Sbt](https://www.scala-sbt.org/download.html)


Run the vscode extension:
* Clone this project
* Open the project in VSCode, run the `import build` task with Metals (it should display a popup automatically).

* run below command, which will open a new VSCode window with the extension loaded(first time it will take some time for scalable typed to convert typescript to scala.js):
```bash
sbt open
```

After the new VSCode (extension development host) window opens:
* Run the Hello World command from the Command Palette (`⇧⌘P`) in the new VSCode window.
* Type `hello` and select `Hello World`.
  * You should see a Notification _Hello World!_.


### Use it as a template
click on the `Use this template` button to create a new repository with the same structure in github.

### Use it as sbt dependency
In your `build.sbt` add the following:
```scala
lazy val vsc = RootProject(uri("https://github.com/doofin/vscode-scalajs-hello.git")) 
lazy val root = Project("root", file(".")) dependsOn(vsc)
```

### Use it as a library
**Currently not working** due to jitpack missing npm! Welcome to contribute to fix it.

You can use this project as a library in your project by adding the following to your `build.sbt`:
```scala
resolvers += Resolver.bintrayRepo("jitpack", "https://jitpack.io")
libraryDependencies += "com.github.doofin" % "vscode-scalajs-hello" % "master-SNAPSHOT" // might be wrong
```

You can find the latest version on
[jitpack.](https://jitpack.io/#doofin/vscode-scalajs-hello)

Note: 
 - I recommend using the Metals extension for Scala in VSCode.
 - If you have any issues, please open an issue on this repository.

## Project structure
The project file structure in src/main/scala is as follows:
```bash
src/main/scala
├── extensionMain.scala // main entry point for the extension
├── commands.scala, codeActions.scala,etc // files for different extension features
│   ├── facade // facade for vscode api
│   ├── io // file and network io functions
```


The project uses the following tools:
* [SBT] build tool for building the project
* [Scala.js] for general coding
* [Scalably Typed] for JavaScript facades
* [scalajs-bundler] for bundling the JavaScript dependencies

SBT is configured with the `build.sbt` file. Scala.js, ScalablyTyped and the bundler are SBT plugins. With these, SBT manages your JavaScript `npm` dependencies. You should never have to run `npm` directly, simply edit the `npmDependencies` settings in `build.sbt`.

[accessible-scala]: https://marketplace.visualstudio.com/items?itemName=scala-center.accessible-scala
[helloworld-minimal-sample]: https://github.com/Microsoft/vscode-extension-samples/tree/master/helloworld-minimal-sample
[Scalably Typed]: https://github.com/ScalablyTyped/Converter
[SBT]: https://www.scala-sbt.org
[ScalaJS]: http://www.scala-js.org
[scalajs-bundler]: https://github.com/scalacenter/scalajs-bundler

## How to code in Scala js?

In general, javascript functions and classes can be used in the same way as in JS/TS!
If the typechecker disagrees, you can insert casts with `.asInstanceOf[Type]`.

The JS types (like `js.Array`) are available from
```scala
import scala.scalajs.js
```

The VSCode classes and functions are available from
```scala
import typings.vscode.mod as vscode

vscode.window.showInformationMessage("Hello World!")
```

Some additional types are available in the `anon` subpackage, for example:
```scala
import typings.vscode.anon.Dispose
// register a command. The cast is necessary due to typescript conversion limitations.
vscode.commands.registerCommand(name, fun).asInstanceOf[Dispose]
```

You can find more information and tutorials on the [Scala.js website](https://www.scala-js.org/).

# references:
 - updated from [vscode-scalajs-hello](https://github.com/pme123/vscode-scalajs-hello) with Scala 3.3.3 and sbt.version=1.9.7.
 - [VSCode Extension Samples](https://github.com/microsoft/vscode-extension-samples) repository.
 - [visualstudio.com/api/get-started](https://code.visualstudio.com/api/get-started/your-first-extension) in typescript.
 - [scalablytyped.com](https://scalablytyped.org/docs/plugin) for the typing plugin.
 - [scala js](https://www.scala-js.org/doc/project/) for the scala.js project.
