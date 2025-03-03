import functorcoder.llm.llmPrompt
import functorcoder.llm.llmPrompt.Prompt
import scala.collection.mutable.ArrayBuffer
import functorcoder.algo.treeParse

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

import io.circe.generic.auto._
import com.doofin.stdScalaCross.TreeNode

TreeNode("root", ArrayBuffer())

val input = "(root [(folder1 [(file1 file2) folder2]) folder3])"
val tree = treeParse.parse(input)
