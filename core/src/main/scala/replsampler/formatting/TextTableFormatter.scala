package replsampler.formatting

import replsampler.Runner.Result

object TextTableFormatter extends Formatter[String] {
  override def apply(in: Seq[Result]): String = {
    val maxSrcWidth = in.map(_.cmd.length).max
    val maxOutputWidth = in.map(_.output.length).max
    in.map(formatOne(_, maxSrcWidth, maxOutputWidth)).mkString
  }

  private def formatOne(in: Result, maxSrcWidth: Int, maxOutputWidth: Int): String = {
    val (cmd, output) = (in.cmd.lines.toList, in.output.lines.toList)
    val maxLines = Seq(cmd, output).map(_.length).max

    val buf = new StringBuilder

    var firstLine = true
    for ((c, o) <- cmd.padTo(maxLines, "").zip(output.padTo(maxLines, ""))) {
      val sep =
        if (firstLine)
          "|=>"
        else
          "|  "
      firstLine = false

      if (o.nonEmpty)
        buf.append(s"${c.padTo(maxSrcWidth, ' ')} $sep $o\n")
      else
        buf.append(s"$c\n")
    }
    //buf.append("-" * maxSrcWidth + " " + "-" * maxOutputWidth + "\n")

    buf.toString()
  }
}
