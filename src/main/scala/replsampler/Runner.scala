package replsampler

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.{Results, IMain}

class Runner {
  val intp = {
    val settings = new Settings

    // Try to build up the classpath for the compiler to use. Since SBT uses a custom classloader
    // we need to detect if we should use it's protocol for carrying over the classpath
    // or just import it from the Java system property.
    if (Option(classOf[Runner].getClassLoader.getResource("app.class.path")).isDefined)
      settings.embeddedDefaults[Runner]
    else
      settings.classpath.value = System.getProperty("java.class.path")

    new IMain(settings)
  }

  private val buf = new StringBuilder

  /**
   * Tries to interpret the given command, adding it to the internal buffer if it is incomplete.
   * @return Some(REPL.Result) if the given statement was complete, otherwise None
   */
  def bufInterpret(cmd: String): Option[Runner.Result] = {
    if (buf.isEmpty && cmd.trim == "")
      return Some(Runner.Result(cmd, "", Runner.Success))

    if (buf.nonEmpty)
      buf.append("\n")
    buf.append(cmd)
    val cmdToRun = buf.toString()

    val (status, stdout) = Util.getWithOut(intp.interpret(cmdToRun))

    if(status != Results.Incomplete) {
      buf.clear()

      Some(Runner.Result(cmdToRun, stdout.trim, status match {
        case Results.Error if !intp.reporter.hasErrors =>
          Runner.RuntimeFail
        case Results.Success =>
          Runner.Success
        case Results.Error =>
          Runner.CompileFail
      }))
    } else None
  }
}

object Runner {
  sealed trait ResultStatus
  sealed trait Fail extends ResultStatus
  case object CompileFail extends Fail
  case object RuntimeFail extends Fail
  case object Success extends ResultStatus

  case class Result(cmd: String, output: String, status: ResultStatus)

}
