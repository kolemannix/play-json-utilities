/* Macro settings */
addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M11" cross CrossVersion.full)
scalacOptions += "-Xplugin-require:macroparadise"
scalacOptions in (Compile, console) ~= (_ filterNot (_ contains "paradise"))
/* Macro settings */

scalaVersion in ThisBuild := "2.12.4"

val scalaMeta = "org.scalameta" %% "scalameta" % "1.8.0"
val playJson = "com.typesafe.play" %% "play-json" % "2.6.9"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % Test
libraryDependencies ++= Seq(
  scalaMeta,
  playJson,
  scalaTest
)

organization := "com.kolemannix"
crossScalaVersions := Seq("2.11.11", "2.12.4")

useGpg := true
