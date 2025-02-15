import functorcoder.llm.llmPrompt
import functorcoder.llm.llmPrompt.Prompt
import fansi.Str
val Modification = llmPrompt
  .Modification(code = "val x = 1", taskRequirement = "add documentation")

Modification.asInstanceOf[Prompt]

sealed trait trait1(val param1: String) {
  // def method1: String = param1
}

case class class1(override val param1: String = "s1") extends trait1(param1)

val c1 = class1()

def m1(t1: trait1) = {
  println(s"t1 param1: ${t1.param1}")
}
// c1.method1
