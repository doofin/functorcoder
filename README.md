# functorcoder
**functorcoder** is an open source AI coding assistant utilizing LLM (Large Language Model) with algebraic and modular design in Scala.js. It's aimed at providing a clean and extensible architecture for AI coding assistants, which is helpful for understanding basic mechanics if you want to build your own AI coding assistant.

features aiming to implement:
- code generation: completion, documentation
- code modification: refactoring, optimization, bug fixing
- code analysis: code understanding, code review, code quality

current features implemented:
- auto completion as you type
- add documentation quick fix action

## Project Structure
The project is divided into two main parts: the core module and the VSCode extension module under /src/main/scala/functorcoder and /src/main/scala/vscextension respectively.

The first part is the core module, containing the main logic of the AI coding assistant:
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
│   │   ├── EditorIntegration.scala # Integration with the editor (e.g., VSCode)
│   └── /tests
│       ├── CoreTests.scala         # Unit tests for core modules
└── /docs
    ├── README.md                 # Project overview and setup instructions
    ├── ARCHITECTURE.md           # Architecture details and design decisions
    └── API.md                    # API documentation for integration
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
