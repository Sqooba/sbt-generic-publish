package io.sqooba.sbt.genericpublish

import org.scalatest.{FlatSpec, Matchers}
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import sbt.internal.util.ManagedLogger

class SbtGenericPublishSpec extends FlatSpec with Matchers with MockitoSugar {

  val logMock: ManagedLogger = mock[ManagedLogger]

  "get host from url" should "get host" in {
    val host = SbtGenericPublish.getHostFromRepoUrl("http://this.shouldwork.com/repo")
    host shouldBe "this.shouldwork.com"
  }

  "get credentials" should "get creds for given host" in {
    val creds = SbtGenericPublish.getCredsForRepo("artifactory.sqooba.io", logMock)
    creds shouldBe a [(String, String)]
  }

  "get deploy path" should "build target url" in {

  }
}
