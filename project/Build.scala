import sbt._
import Keys._

object MyBuild extends Build {
  override val settings = super.settings ++ Seq(
    scalaVersion := "2.11.0-RC3"
  )

  val core = Project("scala-repl-sampler", file("core"))
  val demo = Project("demo", file("demo")) dependsOn core
  val root = Project("root", file(".")) aggregate (core, demo)
}