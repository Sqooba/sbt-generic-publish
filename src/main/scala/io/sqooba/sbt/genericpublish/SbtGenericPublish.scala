package io.sqooba.sbt.genericpublish

import java.io.File

import sbt._
import sbt.internal.util.ManagedLogger
import Keys._

import scala.io.Source
import sys.process._


object SbtGenericPublish extends AutoPlugin {

  object autoImport {
    val genericPublish = inputKey[Unit]("deploy any file necessary")
  }

  import autoImport._


  override lazy val projectSettings: Seq[Setting[_]] = Seq(
    genericPublish := {
      val log = streams.value.log
      val majorScalaVersion = parseScalaMajorVersion(scalaVersion.value)

      log.info(s"ScalaVersion ${scalaVersion.value}")
      val repoAsString = publishTo.value.get.toString()
      val repoUrl = repoAsString.substring(repoAsString.indexOf("http"))

      def parseTargetFileName(): String = s"${name.value}-${version.value}_${majorScalaVersion}${parseFileExtension}"

      def parseDeployPath(): String = {
        val path = organization.value.split('.').mkString("/") + "/" + name.value + "/" + version.value + s"/$parseTargetFileName"
        s"${repoUrl.toString()}/${path}"
      }

      def parseFileExtension(): String = {
        val fileName = artifactPath.value.getName
        fileName.substring(fileName.indexOf("."))
      }

      def deployArtifactToUri(artFile: File, targetUrl: String, credentials: (String, String)) = {
        if (artFile.exists()) {
          log.info(s"Artifact to deploy: ${artFile.getName} exists, deploy to $targetUrl")
          deployFile(artFile, targetUrl, credentials)
        } else {
          log.info(s"Artifact to deploy: ${artFile.getName} does not exists, can't deploy.")
        }
      }

      val artifactAsFile = artifactPath.value
      val deployPath = parseDeployPath
      val creds = getCredsForRepo(getHostFromRepoUrl(repoUrl), log)
      log.info(s"Publish to: $deployPath")
      log.info(s"Credentials: ${creds}")

      deployArtifactToUri(artifactAsFile, deployPath, creds)
    })

  def parseScalaMajorVersion(scalaV: String): String = scalaV.split('.').init.mkString(".")

  def getValFromLine(line: String): String = line.split('=').last

  def deployFile(assFile: File, url: String, creds: (String, String)) = {
    val cmd = s"""curl -u ${creds._1}:${creds._2} -X PUT $url -T ${assFile.getAbsolutePath}"""
    cmd !
  }

  def getHostFromRepoUrl(url: String): String = new URL(url).getHost

  def getCredsForRepo(repoHost: String, log: ManagedLogger): (String, String) = {
    val userHome: String = System.getProperty("user.home")
    val uHome = new File(s"$userHome/.sbt/")
    log.info(s"RepoHost: $repoHost")
    uHome.listFiles()
      .filter(f => f.isFile && f.getName.endsWith(".credentials"))
      .map(Source.fromFile(_).getLines().toList)
      .filter(lines => {
        val hostLine = lines.filter(_.startsWith("host")).head
        getValFromLine(hostLine) == repoHost
      }).map(credFile => {
      val user = credFile(2)
      val pass = credFile(3)
      (getValFromLine(user), getValFromLine(pass))
    }).head
  }

}

/*
override def trigger = allRequirements

    val creds = uHome.listFiles().filter(f => f.isFile && f.getName.endsWith(".credentials")).collect(f => {
      val hostLine = Source.fromFile(f).getLines.filter(_.startsWith("host")).next()
      getValFromLine(hostLine) == repoHost
    })

    val credFile = uHome.listFiles().filter(f => f.isFile && f.getName.endsWith(".credentials")).filter(file => {
      log.info(s"File: ${file.getName}")
      println(s"File: ${file.getName}")
      val hostLine = Source.fromFile(file).getLines.filter(_.startsWith("host")).next()
      val hostInCredFile = getValFromLine(hostLine)
      repoHost.equals(hostInCredFile)
    }).head

// If you change your auto import then the change is automatically reflected here..
import autoImport._


def deploySetting: Setting[_] = publishAssembly := {
  // Sbt provided logger.
  val log = streams.value.log

  if (publishTo.value.isDefined) {
    val majorScalaVersion = parseScalaMajorVersion(scalaVersion.value)
    val url = parseDeploymentUrl(publishTo.value.get, organization.value, name.value, version.value, majorScalaVersion)
    val assemblyFileName = s"${name.value}-assembly-${version.value}.jar"
    val assFile = new File(s"./target/scala-$majorScalaVersion/$assemblyFileName")
    val credTuple = getCreds(log)
    log.info(s"FileName: $assemblyFileName: exists: ${assFile.exists()}")
    log.info(s"TargetUrl: $url")
    log.info(s"Credentians: $credTuple")
    deployFile(assFile, url, credTuple)
  } else {
    log.error("Please define a target repo where to deploy via 'publishTo' -key.")
  }
}

def parseDeploymentUrl(repo: Resolver, org: String, artifactId: String, version: String, majorScalaVersion: String): String = {
  val baseUrl = repo.toString().split(':').tail.mkString(":")
  val path = org.split('.').mkString("/") + s"/$artifactId-assembly_$majorScalaVersion/${version}"
  s"$baseUrl/$path".trim
}

def getCreds(log: ManagedLogger): (String, String) = {
  val userHome: String = System.getProperty("user.home")
  val credFile = new File(s"$userHome/.sbt/.artifactory.sqooba.io.credentials")
  if (credFile.exists()) {
    val lines = Source.fromFile(credFile).getLines.toList
    val user = lines(2)
    val pass = lines(3)
    (getValFromLine(user), getValFromLine(pass))
  } else {
    log.error("Can't find credentials file from ~/.sbt/")
    ("", "")
  }
}

def getValFromLine(line: String): String = line.split('=').last

*/