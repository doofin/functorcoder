package functorcoder.algo

import scala.collection.mutable.ArrayBuffer
import com.doofin.stdScala.dataTypes.Tree.TreeNode
import scala.util.Try

object treeParse {
  val exampleInput = "(root [(folder1 [(file1 file2) folder2]) folder3])"
  val exampleSyntax = "tree := (string [tree tree ...])"

  def parse(input: String): Try[TreeNode[String]] = Try {
    val tokens = tokenize(input)
    val (node, remaining) = parseNode(tokens)
    if (remaining.nonEmpty)
      println(s"Unconsumed tokens: $remaining")
    node
  }

  private def tokenize(input: String): List[String] = {
    val tokenPattern = """(\(|\)|\[|\]|[^\s\(\)\[\]]+)""".r
    tokenPattern.findAllIn(input).toList
  }

  // Parse a node. A node is expected to start with a "(",
  // followed by a value token and then either a bracketed children list
  // or inline children (if any), and finally a ")".
  private def parseNode(tokens: List[String]): (TreeNode[String], List[String]) = tokens match {
    case "(" :: rest =>
      rest match {
        case value :: afterValue =>
          // If the next token is "[", we parse a bracketed children list.
          if (afterValue.nonEmpty && afterValue.head == "[") {
            val (children, afterBracket) = parseChildrenUntil(afterValue.tail, "]")
            afterBracket match {
              case ")" :: tail => (TreeNode(value, children), tail)
              case _           => throw new RuntimeException("Expected ) after children list")
            }
          } else {
            // Otherwise, if the next token is not ")", then we assume inline children.
            if (afterValue.nonEmpty && afterValue.head == ")") {
              // No children case.
              (TreeNode(value), afterValue.tail)
            } else {
              val (children, afterInline) = parseChildrenUntilInline(afterValue)
              (TreeNode(value, children), afterInline)
            }
          }
        case Nil =>
          throw new RuntimeException("Expected node value after (")
      }
    // When not starting with "(", treat the token as a leaf.
    case token :: rest =>
      (TreeNode(token), rest)
    case Nil =>
      throw new RuntimeException("Unexpected end of tokens")
  }

  // Helper: parse children until we reach the given terminator ("]" for bracketed lists).
  // Returns the children (as TreeNode[String]) and the remaining tokens (after dropping the terminator).
  private def parseChildrenUntil(
      tokens: List[String],
      terminator: String
  ): (ArrayBuffer[TreeNode[String]], List[String]) = {
    val children = ArrayBuffer[TreeNode[String]]()
    var rem = tokens
    while (rem.nonEmpty && rem.head != terminator) {
      if (rem.head == "(") {
        val (child, newRem) = parseNode(rem)
        children += child
        rem = newRem
      } else {
        // A plain token becomes a leaf node.
        children += TreeNode(rem.head)
        rem = rem.tail
      }
    }
    if (rem.isEmpty) throw new RuntimeException(s"Expected terminator $terminator")
    (children, rem.tail) // drop the terminator
  }

  private def parseChildrenUntilInline(tokens: List[String]): (ArrayBuffer[TreeNode[String]], List[String]) = {
    val children = ArrayBuffer[TreeNode[String]]()
    var rem = tokens
    while (rem.nonEmpty && rem.head != ")") {
      if (rem.head == "(") {
        val (child, newRem) = parseNode(rem)
        children += child
        rem = newRem
      } else {
        children += TreeNode(rem.head)
        rem = rem.tail
      }
    }
    if (rem.isEmpty) throw new RuntimeException("Expected ) at end of inline children list")
    (children, rem.tail) // drop the closing ")"
  }
}
