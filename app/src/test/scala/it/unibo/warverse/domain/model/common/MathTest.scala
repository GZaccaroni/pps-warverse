package it.unibo.warverse.domain.model.common

import it.unibo.warverse.domain.model.common.Geometry.Polygon
import org.scalatest.Suites
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers
import it.unibo.warverse.domain.model.common.Math.DoubleWithAlmostEquals

object MathTest:
  class DoubleWithAlmostEqualsTest() extends AnyFunSuite with Matchers:
    test("Same value with 0.001 precision must be almost equal") {
      given Math.Precision(0.01)
      (2 ~= 2) mustBe true
    }
    test(
      "Negative and Positive values with 0.001 precision must not be almost equal"
    ) {
      given Math.Precision(0.01)
      (-2 ~= 2) mustBe false
    }
    test("Different value with 0.001 precision must not be almost equal") {
      given Math.Precision(0.01)
      (2 ~= 5) mustBe false
    }
class MathTest
    extends Suites(
      MathTest.DoubleWithAlmostEqualsTest()
    )
