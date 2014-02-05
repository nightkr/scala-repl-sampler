package replsampler

import replsampler.formatting.TextTableFormatter

object Main extends App {
  println(ReplSampler.runAndFormat(
    """
      |class Hello {
      |  val const = "my constant,\nnot yours!"
      |}
      |val h = new Hello
      |println(h.const)
    """.stripMargin, TextTableFormatter))
}
