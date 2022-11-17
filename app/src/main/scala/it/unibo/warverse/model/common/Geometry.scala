package it.unibo.warverse.model.common

import it.unibo.warverse.model.common.Math.DoubleWithAlmostEquals
import java.awt.geom as AwtGeom

object Geometry:

  trait Point[PointType <: Point[PointType]]:
    def distanceFrom(point: PointType): Double

  trait MovablePoint[PointType <: Point[PointType]]:
    def moved(toward: PointType, of: Double): PointType

  case class Point2D(x: Double, y: Double)
      extends Point[Point2D],
        MovablePoint[Point2D]:
    override def distanceFrom(point: Point2D): Double =
      math.sqrt(math.pow(point.x - x, 2) + math.pow(point.y - y, 2))
    override def equals(point: Any): Boolean =
      given Math.Precision(0.1)
      point match
        case point: Point2D => (point.x ~= x) && (point.y ~= y)

    override def moved(toward: Point2D, of: Double): Point2D =
      (toward.x - x, toward.y - y) match
        case (0, 0)                  => Point2D(x, y)
        case (0, yDiff) if yDiff > 0 => Point2D(x, y + of.min(yDiff))
        case (0, yDiff) if yDiff < 0 => Point2D(x, y + (-of).max(yDiff))
        case (xDiff, 0) if xDiff > 0 => Point2D(x + of.min(xDiff), y)
        case (xDiff, 0) if xDiff < 0 => Point2D(x + (-of).max(xDiff), y)
        case (xDiff, yDiff) =>
          val alpha = math.atan2(yDiff, xDiff)
          val distance = distanceFrom(toward)
          val movement = math.min(distance, of)
          val yMovement = movement * math.sin(alpha)
          val xMovement = yMovement / math.tan(alpha)
          Point2D(x + xMovement, y + yMovement)
  trait Polygon[Point]:
    def vertexes: List[Point]
    def contains(point: Point): Boolean

  type Polygon2D = Polygon[Point2D]
  object Polygon2D:
    def apply(vertexes: List[Point2D]): Polygon2D = JavaAwtPolygon2D(vertexes)

  case class JavaAwtPolygon2D(vertexes: List[Point2D]) extends Polygon2D:
    override def contains(point: Point2D): Boolean =
      awtPath2D.contains(point.x, point.y) || vertexes.contains(point);

    private def awtPath2D: AwtGeom.Path2D =
      val path2D = AwtGeom.Path2D.Double()
      vertexes.headOption match
        case Some(firstVertex) =>
          path2D.moveTo(firstVertex.x, firstVertex.y)
          vertexes.tail
            .foreach(vertex => path2D.lineTo(vertex.x, vertex.y))
          path2D
        case None => path2D
