package it.unibo.warverse

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

class MainTest extends AnyFunSuite with Matchers:
  test("example test that succeeds") {
    val obtained = 42
    val expected = 42
    obtained mustBe expected
  }
