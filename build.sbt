sbtPlugin := true

organization := "io.sqooba.sbt"

name := "sbt-generic-publish"

version := "0.1.1-SNAPSHOT"

scalaVersion := "2.12.4"

publishMavenStyle := true

initialCommands in console := "import io.sqooba.sbt.genericpublish._"

sbtVersion in Global := "1.1.0"

libraryDependencies ++= Seq(
  // "org.scala-lang"              %   "scala-library"           % scalaVersion.value,
  "org.scalactic"               %%  "scalactic"               % "3.0.4"   % Test,
  "org.scalatest"               %%  "scalatest"               % "3.0.4"   % Test,
  "org.mockito"                 %   "mockito-all"             % "1.10.19" % Test
)

dependencyOverrides += "org.scala-lang" % "scala-compiler" % scalaVersion.value

publishTo := {
  val realm = "Artifactory Realm"
  val artBaseUrl = "https://artifactory.sqooba.io/artifactory/libs-sbt-local"
  Some(realm at artBaseUrl)
}
