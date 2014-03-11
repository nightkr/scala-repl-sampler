package replsampler

import java.util.Properties

class DependencyFetcher {

}

object DependencyFetcher {
  case class ScalaVersion(version: String, binaryCompatVersion: String)
  object ScalaVersion {
    lazy val default = {
      val properties = new Properties
      val propsStream = getClass.getResourceAsStream("scala-repl-sampler.properties")
      try {
        properties.load(propsStream)
      } finally {
        propsStream.close()
      }
      ScalaVersion(properties.getProperty("scala.version"), properties.getProperty("scala.version.binary"))
    }
  }
}