import replsampler.ReplSampler

object Demo extends App {
  println(ReplSampler.runMacro {
    "a"
    "b"
    def hi(): String = "hello there"
    hi() + "!"
  })
  println(ReplSampler.runMacro("c"))
}
