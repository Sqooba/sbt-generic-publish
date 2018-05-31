import sbt._
import Keys._

sbtPlugin := true

organization := "io.sqooba.sbt"

name := "sbt-generic-publish"

version := "0.1.4.211-SNAPSHOT"

// scalaVersion := "2.10.7"
scalaVersion := "2.11.12"

crossScalaVersions := Seq("2.11.12", "2.10.7")

publishMavenStyle := true

initialCommands in console := "import io.sqooba.sbt.genericpublish._"

// sbtVersion in Global := "1.1.0"

libraryDependencies ++= Seq(
  "org.scalactic"               %%  "scalactic"               % "3.0.5"   % Test,
  "org.scalatest"               %%  "scalatest"               % "3.0.5"   % Test,
  "org.mockito"                 %   "mockito-all"             % "1.10.19" % Test
)

dependencyOverrides += "org.scala-lang" % "scala-compiler" % scalaVersion.value

publishTo := {
  val realm = "Artifactory Realm"
  val artBaseUrl = "https://artifactory.sqooba.io/artifactory/libs-sbt-local"
  Some(realm at artBaseUrl)
}

// sbtVersion in Global := "1.1.6"


/*
scalaCompilerBridgeSource := {
  val sv = appConfiguration.value.provider.id.version
  ("org.scala-sbt" % "compiler-interface" % sv % "component").sources
}
*/
// sbt 0.13.7 version to build for scala 2.11