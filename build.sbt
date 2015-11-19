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
  """ro.purecore.squarecomposer.Logo().main(document.getElementById('squarecomposer-logotype'));
    |ro.purecore.squarecomposer.InitialFigure().main(document.getElementById('initial-figure-1'));
    |ro.purecore.squarecomposer.Transformation01().main(document.getElementById('transformation-1'));
    |ro.purecore.squarecomposer.InitialFigure().main(document.getElementById('initial-figure-2'));
    |ro.purecore.squarecomposer.Transformation02().main(document.getElementById('transformation-2'));
  """.stripMargin

updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)

