package replsampler.formatting

import replsampler.Runner

trait Formatter[T] {
  def apply(in: Seq[Runner.Result]): T
}
