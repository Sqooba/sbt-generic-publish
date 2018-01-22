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

  def deployFile(artifactFile: File, url: String, creds: (String, String)) = {
    val cmd = s"""curl -u ${creds._1}:${creds._2} -X PUT $url -T ${artifactFile.getAbsolutePath}"""
    cmd !
  }

  def getHostFromRepoUrl(url: String): String = new URL(url).getHost

  /**
    * Scans .credential -files in user home directory for matching host.
    * If found returns tuple of (username, password) to be used in the actual publish -action.
    *
    */
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

