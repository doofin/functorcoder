package vscextension

// not working
// Error: Cannot find module 'vscode'

class MySuite1 extends munit.FunSuite {
  test("example test that succeeds") {
    val obtained = 42
    val expected = 42
    assertEquals(obtained, expected)
  }
}
