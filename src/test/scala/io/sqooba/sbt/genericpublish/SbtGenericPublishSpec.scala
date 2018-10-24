package io.sqooba.sbt.genericpublish

import java.io.File

import org.scalatest.{FlatSpec, Matchers}
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar

class SbtGenericPublishSpec extends FlatSpec with Matchers with MockitoSugar {

   // val logMock: ManagedLogger = mock[ManagedLogger]

  "get host from url" should "get host" in {
    val host = SbtGenericPublish.getHostFromRepoUrl("http://this.shouldwork.com/repo")
    host shouldBe "this.shouldwork.com"
  }

  "get credentials" should "get creds for given host" ignore {
    val creds = SbtGenericPublish.getCredsForRepo("artifactory-v2.sqooba.io")
    creds shouldBe a [(_, _)]
    creds._1 shouldBe a [String]
    creds._2 shouldBe a [String]
  }

  "get deploy path" should "build target url" in {
    val repoUrl: String = "http://some.repo.com"
    val org: String = "myorg"
    val nameS: String = "projname"
    val vers: String = "1.1.1"
    val fileName: String = "somefile.txt"
    val depUrl = SbtGenericPublish.parseRepoDeployPath(repoUrl, org, nameS, vers, fileName)
    depUrl shouldBe "http://some.repo.com/myorg/projname/1.1.1/somefile.txt"
  }

  "create pack" should "for few files" in {
    val targetFile = new File("src/test/resources/test.zip")
    val f1Path = "src/test/resources/test1.txt"
    val f2Path = "src/test/resources/test2.txt"
    val f1 = new File(f1Path)
    val f2 = new File(f2Path)

    f1.isFile shouldBe true
    f2.isFile shouldBe true
    f1.exists() shouldBe true
    f2.exists() shouldBe true

    val zipFile = SbtGenericPublish.zip(targetFile, Seq(f1Path, f2Path), false)
    zipFile.exists() shouldBe true
    zipFile.delete()
    zipFile.exists() shouldBe false
  }

  "exec runtime command" should "do something" in {
    val rt = java.lang.Runtime.getRuntime
    val res = sys.process.Process("ls -la").!
    val cli = res.toString
    println(s"cli: $cli")

  }
}
