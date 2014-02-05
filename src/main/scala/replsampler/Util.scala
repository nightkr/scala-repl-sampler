package replsampler

import java.io.{StringWriter, OutputStream}

object Util {
  class StringWriterOutputStream {
    val writer = new StringWriter

    object out extends OutputStream {
      override def write(x: Int) {
        writer.write(x)
      }
    }
  }

  def getWithOut[T](f: => T): (T, String) = {
    val s = new StringWriterOutputStream

    val retval = Console.withErr(s.out) {
      Console.withOut(s.out) {
        f
      }
    }

    (retval, s.writer.toString)
  }
}
