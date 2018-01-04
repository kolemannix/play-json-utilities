/* Macro settings */
addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M10" cross CrossVersion.full)
scalacOptions += "-Xplugin-require:macroparadise"
scalacOptions in (Compile, console) ~= (_ filterNot (_ contains "paradise"))
/* Macro settings */

val scalaMeta = "org.scalameta" %% "scalameta" % "1.8.0"
val playJson = "com.typesafe.play" %% "play-json" % "2.6.6"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test
libraryDependencies ++= Seq(
  scalaMeta,
  playJson,
  scalaTest
)

organization := "com.kolemannix"
crossScalaVersions := Seq("2.11.11", "2.12.4")

useGpg := true
