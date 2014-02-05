package replsampler

object Main extends App {
  val repl = new Runner
  println(repl.bufInterpret("class Hello"))
  println(repl.bufInterpret("class"))
  println(repl.bufInterpret("Greetings {"))
  println(repl.bufInterpret("}"))
  println(repl.bufInterpret("throw new Exception"))
  println(repl.bufInterpret("val lastException: String = 1"))
}
