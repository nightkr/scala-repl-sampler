import java.util.Properties
import java.io.FileOutputStream

scalaVersion := "2.10.3"

libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value

libraryDependencies += "org.scalatest" %% "scalatest" % "2.0" % "test"

libraryDependencies += "org.apache.ivy" % "ivy" % "2.3.0"

resourceGenerators in Compile <+= Def.task {
  val file = (resourceManaged in Compile).value / "scala-repl-sampler.properties"
  val props = new Properties
  props.setProperty("scala.version.binary", scalaBinaryVersion.value)
  props.setProperty("scala.version", scalaVersion.value)
  IO.createDirectory(file.getParentFile)
  val fos = new FileOutputStream(file)
  try {
    props.store(fos, "")
  } finally {
    fos.close()
  }
  Seq(file)
}

mappings in (Compile, packageBin) += {
  (baseDirectory.value / "scala-repl-sampler.properties") -> "scala-repl-sampler.properties"
}