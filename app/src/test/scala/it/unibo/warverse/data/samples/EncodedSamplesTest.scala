package it.unibo.warverse.data.samples

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

class EncodedSamplesTest() extends AnyFunSuite with Matchers:
  private val encodedSamples = EncodedSamples()

  test("complete simulation environment sample generation must succeed") {
    checkResult(() => encodedSamples.complete)
  }
  test("country sample generation must succeed") {
    checkResult(() => encodedSamples.country)
  }
  test("country relations sample generation must succeed") {
    checkResult(() => encodedSamples.countryRelations)
  }
  test("army unit sample generation must succeed") {
    checkResult(() => encodedSamples.armyUnit)
  }
  test("army unit kind sample generation must succeed") {
    checkResult(() => encodedSamples.armyUnitKind)
  }
  test("country boundaries sample generation must succeed") {
    checkResult(() => encodedSamples.countryBoundaries)
  }
  private def checkResult(fun: () => String): Unit =
    noException mustBe thrownBy(fun)
    fun() mustNot be(null)
