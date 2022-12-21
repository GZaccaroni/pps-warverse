package it.unibo.warverse.domain.engine.prolog

import it.unibo.warverse.domain.model.common.Geometry.Point2D
import it.unibo.warverse.domain.model.common.Math
import org.scalatest.Suites
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

object PrologPredicatesTest:
  class DistanceBetweenTest() extends AnyFunSuite with Matchers:
    test("Distance between two equal points must be zero") {
      val point = Point2D(0, 5)
      val distance = PrologPredicates.distanceBetween(point, point)
      distance mustBe 0
    }
    test("Distance must be computed correctly") {
      val point1A = Point2D(0, 5)
      val point1B = Point2D(0, 10)
      val distance1 = PrologPredicates.distanceBetween(point1A, point1B)
      distance1 mustBe 5

      val point2A = Point2D(5, 0)
      val point2B = Point2D(10, 0)
      val distance2 = PrologPredicates.distanceBetween(point2A, point2B)
      distance2 mustBe 5

      val point3A = Point2D(0, 0)
      val point3B = Point2D(10, 10)
      val distance3 = PrologPredicates.distanceBetween(point3A, point3B)
      distance3 mustBe (10 * math.sqrt(2))
    }

  class ReachableSortedTargetsTest() extends AnyFunSuite with Matchers:
    private val point = Point2D(0, 0)
    private val targetsRange5 =
      List(Point2D(0, 3), Point2D(0, 5), Point2D(4, 0), Point2D(0, 0))
    private val targetsRangeGreaterThan5 =
      List(Point2D(20, 9), Point2D(8, 8), Point2D(10, 0), Point2D(6, 8))

    test(
      "Reachable sorted targets with max radius and max limit must return all results"
    ) {
      val result = PrologPredicates.reachableSortedTargets(
        point,
        Double.MaxValue,
        targetsRange5 ++ targetsRangeGreaterThan5,
        Int.MaxValue
      )
      result mustBe List(
        Point2D(0, 0),
        Point2D(0, 3),
        Point2D(4, 0),
        Point2D(0, 5),
        Point2D(10, 0),
        Point2D(6, 8),
        Point2D(8, 8),
        Point2D(20, 9)
      )
    }
    test(
      "Reachable sorted targets with max radius and 0 limit must return no results"
    ) {
      val result = PrologPredicates.reachableSortedTargets(
        point,
        Double.MaxValue,
        targetsRange5 ++ targetsRangeGreaterThan5,
        0
      )
      result.isEmpty mustBe true
    }
    test(
      "Reachable sorted targets with max radius and 1 limit must return only first result"
    ) {
      val result = PrologPredicates.reachableSortedTargets(
        point,
        Double.MaxValue,
        targetsRange5 ++ targetsRangeGreaterThan5,
        1
      )
      result mustBe List(Point2D(0, 0))
    }
    test(
      "Reachable sorted targets with 5 radius and max limit must return only targetsRange5 results"
    ) {
      val result = PrologPredicates.reachableSortedTargets(
        point,
        5,
        targetsRange5 ++ targetsRangeGreaterThan5,
        Int.MaxValue
      )
      result mustBe List(
        Point2D(0, 0),
        Point2D(0, 3),
        Point2D(4, 0),
        Point2D(0, 5)
      )
    }
class PrologPredicatesTest
    extends Suites(
      PrologPredicatesTest.DistanceBetweenTest(),
      PrologPredicatesTest.ReachableSortedTargetsTest()
    )
