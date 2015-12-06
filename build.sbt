import com.lihaoyi.workbench.Plugin._

enablePlugins(ScalaJSPlugin)

workbenchSettings

name := "SquareComposer"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.8.0",
  "com.lihaoyi" %%% "scalatags" % "0.5.2"
)

bootSnippet :=
  """ro.purecore.squarecomposer.SquareComposer().main(
    |  document.getElementById('squarecomposer-logotype'),
    |  document.getElementById('main'));"""
    .stripMargin

updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)

