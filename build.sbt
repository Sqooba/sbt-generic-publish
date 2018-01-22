sbtPlugin := true

organization := "io.sqooba.sbt"

name := "sbt-generic-publish"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.4"

publishMavenStyle := true

initialCommands in console := "import io.sqooba.sbt.genericpublish._"

publishTo := {
  val realm = "Artifactory Realm"
  val artBaseUrl = "https://artifactory.sqooba.io/artifactory/libs-sbt-local"
  Some(realm at artBaseUrl)
}

libraryDependencies ++= Seq(
  "org.scala-lang"              %   "scala-library"           % scalaVersion.value,
  "org.scalactic"               %%  "scalactic"               % "3.0.4"   % Test,
  "org.scalatest"               %%  "scalatest"               % "3.0.4"   % Test,
  "org.mockito"                 %   "mockito-all"             % "1.10.19" % Test
)

dependencyOverrides += "org.scala-lang" % "scala-compiler" % scalaVersion.value


/* -- causes 409 conflict for some reason, so use sbt local for now
publishTo := {
  val realm = "Artifactory Realm"
  val artBaseUrl = "https://artifactory.sqooba.io/artifactory"
  if (isSnapshot.value) {
    Some(realm at s"$artBaseUrl/libs-snapshot-local;build.timestamp=" + new java.util.Date().getTime)
  } else {
    Some(realm at s"$artBaseUrl/libs-release-local")
  }
}
*/
