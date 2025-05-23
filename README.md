# functorcoder
**functorcoder** is an open source AI coding assistant utilizing LLM (Large Language Model) with algebraic and modular design in Scala.js. It aims at providing a clean and extensible architecture for AI coding assistants, which is helpful for understanding basic mechanics if you want to build your own AI coding assistant.

current features implemented:
- auto completion as you type
- add documentation quick fix action

next important features to be implemented:
- generate multiple files and folders
- disable/enable auto completion

features aiming to implement in long term:
- code generation: completion, documentation
- code modification: refactoring, optimization, bug fixing
- code analysis: code understanding, code review, code quality

## 中文简介
作为一个copilot的用户，最近受到国产开源模型deepseek的鼓励，希望能在开源社区中贡献一些自己的力量。目前已经有一些ai插件，比如copilot, tabnine，cursor等，还有一些开源的插件，比如continue。我看了下continue的代码，发现它的设计很复杂，代码并不简洁。目前，copilot的体验还可以，但是非常封闭，无法自定义很多地方，比如代码补全的长度，模型的选择等。开源的插件则有很多稳定性问题，bug不少。

所以，作为一个scala的爱好者，也希望加深对llm应用的理解，我决定自己用scala.js来实现一个简单的ai助手。

## Getting Started
Visit [vscode-scalajs-hello](https://github.com/doofin/vscode-scalajs-hello) to understand how to play with scala.js for VSCode extension development. Basically, sbt is used to build the project and run the extension. There you will learn:
- setting up the development environment
- building the project and running the extension
- packaging the extension


Before loading the extension, you need to add options to vscode user settings, and provide your OpenAI compatible API key and URL. Here is an example:

```json
"functorcoder": {
        "apiKey": "somekey",
        "apiUrl": "https://api.openai.com/v1/chat/completions",
        "maxTokens": 512,
        "model": "gpt-4o-mini",
    }
```

## Project Structure
The project is divided into two main parts: the core module and the VSCode extension module under /src/main/scala/functorcoder and /src/main/scala/vscextension respectively.

**To get started**, read the file `extensionMain.scala` in the VSCode extension module. It is the main entry point for the extension. 

The first part is the core module, we aim keeping it concise. It contains the main logic of the ai coding assistant:
- Large Language Model (LLM) integration
- sending propmt to LLM and getting the response

The second part is the VSCode extension module, which integrates the core module with the VSCode editor. It contains:
- commands: commands to be executed in the editor
- code actions: quick fix actions
- code completion: auto completion
- editor ui: status bar, notifications, etc.
  
It's adopted from the [vscode-scalajs-hello](https://github.com/doofin/vscode-scalajs-hello) project. Refer to it for getting started with the VSCode extension development in Scala.js.


project file structure for the core module:
```bash
/functorcoder
├── /src/main/scala/functorcoder
│   ├── /llm                 # Integration with LLM (e.g., OpenAI API)
│   ├── /actions
│   │   ├── CodeGen.scala    # Code completion, generation, and documentation
│   │   ├── Commands.scala   # Commands from functorcoder
│   │   └── Debug.scala      # Debugging module
│   ├── /types               # Types for code, context, and user actions
│   ├── /editorUI            # Integration with the editor (e.g., VSCode)
│   └── /tests               # Unit tests for core modules
└── /docs                    # Documentation
```

The project file structure for the VSCode extension module:
```bash
/vscextension
├── /src/main/scala/vscextension
│   ├── extensionMain.scala       # Main entry point for the extension
│   ├── commands.scala            # Command definitions
│   ├── codeActions.scala         # Code action definitions
...
```


## design principles
I am to design the system with mathematics, algebra and functional programming principles in mind. The system is designed to be modular and extensible, allowing for easy addition of new features and components.

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

# feedback
features to be implemented:
- refactoring
- specify which LLM to use
- RAG(retrieval-augmented generation) to understand the whole code base
- MCP(model context protocol) to interact with the environment, like external tools, etc.


# references:
 - updated from [vscode-scalajs-hello](https://github.com/pme123/vscode-scalajs-hello) with Scala 3.3.3 and sbt.version=1.9.7.
 - [VSCode Extension Samples](https://github.com/microsoft/vscode-extension-samples) repository.
 - [visualstudio.com/api/get-started](https://code.visualstudio.com/api/get-started/your-first-extension) in typescript.
 - [scalablytyped.com](https://scalablytyped.org/docs/plugin) for the typing plugin.
 - [scala js](https://www.scala-js.org/doc/project/) for the scala.js project.
