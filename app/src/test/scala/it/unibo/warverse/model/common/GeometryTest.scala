package it.unibo.warverse.model.common

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers
import it.unibo.warverse.model.common.Geometry
import org.scalatest.Suites;

object GeometryTest:
  class Point2DTest() extends AnyFunSuite with Matchers:
    private val initialPoint1 = Geometry.Point2D(x = 0, y = 0)
    private val destinationOnXAxis = Geometry.Point2D(x = 5, y = 0)
    private val destinationOnYAxis = Geometry.Point2D(x = 0, y = 5)
    private val destinationOnXYAxis = Geometry.Point2D(x = 5, y = 5)

    test("Point should not move if already reached destination") {
      val point = Geometry.Point2D(x = 0, y = 0)
      val movedPoint = point.moved(toward = point, of = 10)
      point mustBe movedPoint
    }
    test("Distance between points should be valid") {
      initialPoint1.distanceFrom(point = initialPoint1) mustBe
        0
      initialPoint1.distanceFrom(point = destinationOnXAxis) mustBe
        5
      initialPoint1.distanceFrom(point = destinationOnYAxis) mustBe
        5
      initialPoint1.distanceFrom(point = destinationOnXYAxis) mustBe
        (5 * math.sqrt(2))
    }
    test("Point shouldn't move if no movement set") {
      initialPoint1.moved(toward = destinationOnXAxis, of = 0) mustBe
        initialPoint1
    }
    test("Point moves on x axis towards destination") {
      initialPoint1.moved(toward = destinationOnXAxis, of = 3) mustBe
        Geometry.Point2D(x = 3, y = 0)
      initialPoint1.moved(toward = destinationOnXAxis, of = 5) mustBe
        destinationOnXAxis
      initialPoint1.moved(toward = destinationOnXAxis, of = 10) mustBe
        Geometry.Point2D(x = 5, y = 0)
    }
    test("Point moves on y axis towards destination") {
      initialPoint1.moved(toward = destinationOnYAxis, of = 3) mustBe
        Geometry.Point2D(x = 0, y = 3)
      initialPoint1.moved(toward = destinationOnYAxis, of = 5) mustBe
        destinationOnYAxis
      initialPoint1.moved(toward = destinationOnYAxis, of = 10) mustBe
        destinationOnYAxis
    }
    test("Point moves on x and y axes towards destination") {
      initialPoint1.moved(
        toward = destinationOnXYAxis,
        of = 3 * math.sqrt(2)
      ) mustBe
        Geometry.Point2D(x = 3, y = 3)
      initialPoint1.moved(
        toward = destinationOnXYAxis,
        of = 5 * math.sqrt(2)
      ) mustBe
        destinationOnXYAxis
      initialPoint1.moved(
        toward = destinationOnXYAxis,
        of = 10
      ) mustBe
        destinationOnXYAxis
    }
  class Polygon2DTest() extends AnyFunSuite with Matchers:
    private val polygon = Geometry.Polygon2D(vertexes =
      List(
        Geometry.Point2D(x = 0, y = 0),
        Geometry.Point2D(x = 0, y = 5),
        Geometry.Point2D(x = 5, y = 5),
        Geometry.Point2D(x = 5, y = 0)
      )
    )
    test("Polygon contains point") {
      polygon.contains(Geometry.Point2D(0, 0)) mustBe true
      polygon.contains(Geometry.Point2D(2, 2)) mustBe true
      polygon.contains(Geometry.Point2D(5, 5)) mustBe true
    }
    test("Polygon center is valid") {
      polygon.center mustBe Geometry.Point2D(2.5, 2.5)
    }
    test("Polygon doesn't contain point") {
      polygon.contains(Geometry.Point2D(10, 10)) mustBe false
    }

class GeometryTest
    extends Suites(
      GeometryTest.Point2DTest(),
      GeometryTest.Polygon2DTest()
    )
