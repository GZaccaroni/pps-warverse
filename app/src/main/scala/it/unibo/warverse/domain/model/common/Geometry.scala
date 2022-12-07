package it.unibo.warverse.domain.model.common

import it.unibo.warverse.domain.model.common.Math.DoubleWithAlmostEquals
import java.awt.geom as AwtGeom

object Geometry:
  /** Represents an n-dimensional Point
    * @tparam PointType
    *   the actual type of the Point
    */
  trait Point[PointType <: Point[PointType]](val coordinates: Seq[Double]):

    def distanceFrom(point: PointType): Double =
      math.sqrt(
        coordinates
          .zip(point.coordinates)
          .map((thisCoord, pointCoord) => math.pow(thisCoord - pointCoord, 2))
          .sum
      )

    override def equals(obj: Any): Boolean =
      given Math.Precision(0.1)
      obj match
        case point: Point[?] =>
          coordinates
            .zip(point.coordinates)
            .forall((thisCoord, pointCoord) => thisCoord ~= pointCoord)
  /** Represents an n-dimensional Point that can be moved
    *
    * @tparam PointType
    *   the actual type of the Point
    */
  trait MovablePoint[PointType <: Point[PointType]]:
    /** Creates another point moved in direction of `toward` point of `of`
      * distance
      * @param toward
      *   the direction in which the point should be moved
      * @param of
      *   the distance the point should be moved
      * @return
      *   The moved point
      */
    def moved(toward: PointType, of: Double): PointType

  /** A 2-Dimensional implementation of Point2D
    * @param x
    *   the x coordinate of point
    * @param y
    *   the y coordinate of point
    */
  case class Point2D(x: Double, y: Double)
      extends Point[Point2D](Seq(x,y)),
        MovablePoint[Point2D]:

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

  /** A geometric polygon
    * @tparam Point
    *   the type of the vertexes of the polygon
    */
  trait Polygon[Point]:
    /** Returns the vertexes of the polygon
      * @return
      *   the vertexes of the polygon
      */
    def vertexes: Seq[Point]

    /** Returns the center of the polygon
      *
      * @return
      *   the center of the polygon
      */
    def center: Point

    /** Returns the area of the polygon
      *
      * @return
      *   the area of the polygon
      */
    def area: Double

    /** It checks if the polygon contains a given point
      *
      * @return
      *   true if the polygon contains the {@link Point} else false
      */
    def contains(point: Point): Boolean

  object Polygon:
    /** Builds a 2-Dimensional polygon
      * @param vertexes
      *   vertexes of the polygon
      * @return
      *   a {@link Polygon2D}
      */
    def apply(vertexes: Seq[Point2D]): Polygon[Point2D] = JavaAwtPolygon2D(
      vertexes
    )

    private case class JavaAwtPolygon2D(vertexes: Seq[Point2D])
        extends Polygon[Point2D]:
      override def contains(point: Point2D): Boolean =
        awtPath2D.contains(point.x, point.y) || vertexes.contains(point);

      override def center: Point2D =
        Point2D(
          vertexes.map(_.x).sum / vertexes.length,
          vertexes.map(_.y).sum / vertexes.length
        )

      /** Compute te area with the Sohelace formula
        *
        * @return
        *   the area of Polygon2D
        */
      override def area: Double =
        val X = vertexes.map(_.x)
        val Y = vertexes.map(_.y)
        (for
          i <- vertexes.indices
          j = (i - 1 + vertexes.length) % vertexes.length
          area = (X(j) + X(i)) * (Y(j) - Y(i))
        yield area).sum / 2

      private def awtPath2D: AwtGeom.Path2D =
        val path2D = AwtGeom.Path2D.Double()
        vertexes.headOption match
          case Some(firstVertex) =>
            path2D.moveTo(firstVertex.x, firstVertex.y)
            vertexes.tail
              .foreach(vertex => path2D.lineTo(vertex.x, vertex.y))
            path2D
          case None => path2D
