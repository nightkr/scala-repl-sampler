package replsampler

import replsampler.formatting.Formatter
import replsampler.Runner.Result
import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros

object ReplSampler {
  def runAndFormat[T](cmds: String, f: Formatter[T]): T = f(runAll(cmds))

  def runAll(cmds: String): Seq[Result] = {
    val r = new Runner
    cmds.lines.flatMap(r.bufInterpret(_).toSeq).toSeq
  }

  def runMacro[A](in: A): Seq[Runner.Result] = macro ReplSamplerImpl.run
}

class ReplSamplerImpl(val c: Context) {
  import c.universe._

  def run(in: Tree): Tree = {
    val q"{..$stmts}" = in

    val runner = new Runner(Some(c.classPath))

    val evaled = for {
      x <- stmts
    } yield {
      val cmd = showCode(x)
      runner.interpretStatement(cmd)
    }

    q"Seq(..$evaled)"
  }

  implicit val resultLiftable = {
    val resultSym = symbolOf[Runner.Result].companion
    Liftable[Runner.Result] { r =>
      q"$resultSym(${r.cmd}, ${r.output}, ${r.status})"
    }
  }

  implicit val resultStatusLiftable: Liftable[Runner.ResultStatus] = {
    import Runner._
    val runnerSym = symbolOf[Runner].companion
    Liftable[ResultStatus] {
      case CompileFail => q"$runnerSym.CompileFail"
      case RuntimeFail => q"$runnerSym.RuntimeFail"
      case Success => q"$runnerSym.Success"
    }
  }
}

