package it.unibo.warverse.domain.model.common

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers
import it.unibo.warverse.domain.model.common.Geometry
import it.unibo.warverse.domain.model.common.Geometry.Polygon2D
import org.scalatest.Suites

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
    test("Polygon center is valid") {
      polygon.center mustBe Geometry.Point2D(2.5, 2.5)
    }

    test("Polygon area is valid") {
      polygon.area mustBe 25
    }

    test("Polygon contains point") {
      polygon.contains(Geometry.Point2D(0, 0)) mustBe true
      polygon.contains(Geometry.Point2D(2, 2)) mustBe true
      polygon.contains(Geometry.Point2D(5, 5)) mustBe true
    }

    test("Polygon doesn't contain point") {
      polygon.contains(Geometry.Point2D(10, 10)) mustBe false
    }

  class MultiPolygon2DTest() extends AnyFunSuite with Matchers:
    private val multiPolygon = Geometry.MultiPolygon2D(
      Seq(
        Polygon2D(
          Seq(
            Geometry.Point2D(x = 0, y = 0),
            Geometry.Point2D(x = 0, y = 5),
            Geometry.Point2D(x = 5, y = 5),
            Geometry.Point2D(x = 5, y = 0)
          )
        ),
        Polygon2D(
          Seq(
            Geometry.Point2D(x = 10, y = 0),
            Geometry.Point2D(x = 10, y = 5),
            Geometry.Point2D(x = 20, y = 5),
            Geometry.Point2D(x = 20, y = 0)
          )
        )
      )
    )
    test("MultiPolygon area is valid") {
      multiPolygon.area mustBe (25 + 50)
    }

    test("MultiPolygon contains point") {
      multiPolygon.contains(Geometry.Point2D(0, 0)) mustBe true
      multiPolygon.contains(Geometry.Point2D(2, 2)) mustBe true
      multiPolygon.contains(Geometry.Point2D(5, 5)) mustBe true
      multiPolygon.contains(Geometry.Point2D(10, 0)) mustBe true
      multiPolygon.contains(Geometry.Point2D(12, 2)) mustBe true
      multiPolygon.contains(Geometry.Point2D(20, 5)) mustBe true
    }

    test("MultiPolygon doesn't contain point") {
      multiPolygon.contains(Geometry.Point2D(50, 50)) mustBe false
      multiPolygon.contains(Geometry.Point2D(0, 10)) mustBe false
    }

    test("MultiPolygon split generate correct number of splits") {
      multiPolygon.split(1).length mustBe 1
      multiPolygon.split(2).length mustBe 2
      multiPolygon.split(3).length mustBe 3
    }

class GeometryTest
    extends Suites(
      GeometryTest.Point2DTest(),
      GeometryTest.Polygon2DTest(),
      GeometryTest.MultiPolygon2DTest()
    )
