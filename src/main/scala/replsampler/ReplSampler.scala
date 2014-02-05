package replsampler

import replsampler.formatting.Formatter
import replsampler.Runner.Result

object ReplSampler {
  def runAndFormat[T](cmds: String, f: Formatter[T]): T = f(runAll(cmds))

  def runAll(cmds: String): Seq[Result] = {
    val r = new Runner
    cmds.lines.flatMap(r.bufInterpret(_).toSeq).toSeq
  }
}
