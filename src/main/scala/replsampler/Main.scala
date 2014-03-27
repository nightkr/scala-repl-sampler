package replsampler

import replsampler.formatting.TextTableFormatter
import replsampler.DependencyFetcher.Module

object Main extends App {
  val ivy = IvyDependencyFetcher(DependencyFetcher.ScalaVersion.default)
  println(ivy.getAll(Seq(Module("org.scala-lang", "scala-compiler", "2.10.3", scala = false))))
  /*println(ReplSampler.runAndFormat(
    """
      |class Hello {
      |  val const = "my constant,\nnot yours!"
      |}
      |val h = new Hello
      |println(h.const)
    """.stripMargin, TextTableFormatter))*/
}
