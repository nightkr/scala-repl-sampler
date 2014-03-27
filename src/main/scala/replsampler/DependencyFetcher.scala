package replsampler

import java.util.Properties

trait DependencyFetcher {
}

object DependencyFetcher {
  case class ScalaVersion(version: String, binaryCompatVersion: String)
  object ScalaVersion {
    lazy val default: ScalaVersion = {
      val properties = new Properties
      val propsStream = Option(getClass.getResourceAsStream("/scala-repl-sampler.properties"))
      try {
        properties.load(propsStream.get)
      } finally {
        propsStream.foreach(_.close())
      }
      ScalaVersion(properties.getProperty("scala.version"), properties.getProperty("scala.version.binary"))
    }
  }

  case class Module(group: String, module: String, version: String, scala: Boolean = false) {
    def fullModule(scalaVersion: ScalaVersion): String =
      if(scala)
        s"${module}_$version"
      else
        module
  }

  def default: DependencyFetcher = IvyDependencyFetcher(ScalaVersion.default)
}