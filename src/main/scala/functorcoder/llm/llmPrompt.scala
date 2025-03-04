package functorcoder.llm

/** prompts for the llm
  *
  * for completion, code generation, etc.
  */
object llmPrompt {

  /** tags, placeholders and templates used in the prompt
    *
    * for code completion
    */
  case class QueryTags(
      hole: String, //
      queryStart: String,
      queryEnd: String,
      task: String
  )

  val tagsInUse =
    QueryTags(
      hole = "{{HOLE}}", //
      queryStart = "{{QUERY_START}}",
      queryEnd = "{{QUERY_END}}",
      task = "{{TASK}}"
    )

  // trait will have undefined value, so we use abstract class
  sealed abstract class Prompt(val assistantMsg: String) {
    def generatePrompt: String
    def getAssistantMessage: String = assistantMsg
  }

  /** code completion prompt
    *
    * https://github.com/continuedev/continue/blob/main/core/autocomplete/templating/AutocompleteTemplate.ts
    *
    * @param codeWithHole
    *   code with a hole to fill like {{FILL_HERE}}
    * @param taskRequirement
    *   like "Fill the {{FILL_HERE}} hole."
    * @param assistantMessage
    *   like "always give scala code examples." or
    *
    * You are a HOLE FILLER. You are provided with a file containing holes, formatted as '{{HOLE_NAME}}'. Your TASK is
    * to complete with a string to replace this hole with, inside a <COMPLETION/> XML tag, including context-aware
    * indentation, if needed
    *
    * You will complete code strings with a hole {{FILL_HERE}}, you only return the code for the hole.
    */
  case class Completion(
      codeWithHole: String, // code with a hole to fill like {{FILL_HERE}}
      // taskRequirement: String, // like "Fill the {{FILL_HERE}} hole."
      assistantMessage: String = promptText.prompt1
  ) extends Prompt(assistantMessage) {
    def generatePrompt = {

      codeWithHole
    }

  }

  /** modify code snippet
    *
    * @param code
    *   code snippet
    * @param taskRequirement
    *   like "Fill the {{FILL_HERE}} hole."
    * @param assistantMessage
    *   like "always give scala code examples."
    */
  case class Modification(
      code: String,
      taskRequirement: String,
      assistantMessage: String =
        "You are given a text or code snippet wrapped in <QUERY> tag and a TASK requirement. " +
          "You are going to return the new snippet according to the TASK requirement. "
  ) extends Prompt(assistantMessage) {
    def generatePrompt = {
      s"""<QUERY>
    |${code}
    |</QUERY>
    |TASK: ${taskRequirement}
    |""".stripMargin
    }
  }
  case class CreateFiles(
      userRequest: String,
      assistantMessage: String =
        s"You are given a user requirement wrapped in ${tagsInUse.queryStart} and ${tagsInUse.queryEnd}, and a TASK requirement ${tagsInUse.task}. " +
          "You are going to return the code snippet according to the TASK requirement. "
  ) extends Prompt(assistantMessage) {
    def generatePrompt = {
      import functorcoder.algo.treeParse

      val task =
        s"parse the prompt response to tree of files and folders in the format: ${treeParse.exampleSyntax}. An example input is: ${treeParse.exampleInput}. return the tree data structure in that format."

      s"""${tagsInUse.queryStart}
    |${userRequest}
    |${tagsInUse.queryEnd}
    |${tagsInUse.task} : ${task}
    |""".stripMargin
    }
  }

  /** prompts engineering
    *
    * more like art than science. just try different prompts and see what works best
    */
  object promptText {
    val hole = "{{FILL_HERE}}"
    val prompt1 =
      "You are a code or text autocompletion assistant. " +
        s"In the provided input, missing code or text are marked as $hole. " +
        "Your task is to output only the snippet that replace the placeholder, " +
        "ensuring that indentation and formatting remain consistent with the context. Don't quote your output"
    val prompt2 =
      "You are a hole filler," +
        "You are given a string with a hole: " +
        s"$hole  in the string, " +
        "your task is to replace this hole with your reply." +
        "you only return the string for the hole with indentation, without any quotes"

  }

  def generateDocs(language: String) = {
    "generate short documentation for the input code, " +
      "and return only the documentation for language: " + language +
      "the documentation shall be the format according to the language, " +
      "but don't wrap it with backticks or any other tags."
  }
}

/* example:
<QUERY>
function sum_evens(lim) {
  var sum = 0;
  for (var i = 0; i < lim; ++i) {
    {{FILL_HERE}}
  }
  return sum;
}
</QUERY>

TASK: Fill the {{FILL_HERE}} hole.

## CORRECT COMPLETION

<COMPLETION>if (i % 2 === 0) {
      sum += i;
    }</COMPLETION>

## EXAMPLE QUERY:

<QUERY>
def sum_list(lst):
  total = 0
  for x in lst:
  {{FILL_HERE}}
  return total

print sum_list([1, 2, 3])
</QUERY>

## CORRECT COMPLETION:

<COMPLETION>  total += x</COMPLETION>

 */
