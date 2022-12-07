package it.unibo.warverse.domain.model.common

import it.unibo.warverse.domain.model.common.Math.DoubleWithAlmostEquals

import java.awt.geom as AwtGeom
import java.util

object Geometry:

  /** Represent a generic point in a space.
    * @tparam PointType
    *   the type of the point
    */
  trait Point[PointType <: Point[PointType]]:
    /** It computes the distance between this and a point given by param.
      *
      * @param point
      *   the point from which calculate the distance
      * @return
      *   the distance between the two points
      */
    def distanceFrom(point: PointType): Double

  /** Represent a generic point that can be moved in a space
    * @tparam PointType
    *   the type of the point
    */
  trait MovablePoint[PointType <: Point[PointType]]:
    /** It computes a new point that is this moved toward the given one of the
      * given distance
      * @param toward
      *   a point that indicates the direction of movement
      * @param of
      *   measure of the distance crossed in the movement
      * @return
      *   a new point in the resulted position
      */
    def moved(toward: PointType, of: Double): PointType

  /** Represent an implementation of [[Point]] and [[MovablePoint]] in a two
    * dimensional space
    * @param x
    *   the coordinate along the x axis
    * @param y
    *   the coordinate along the y axis
    */
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

  /** Represent a polygon with generic definition of Point
    * @tparam Point
    *   the type of point that describes the polygon
    */
  trait Polygon[Point]:
    /** The sequence of vertexes that describes the boundaries of the polygon
      * @return
      *   the sequence of vertexes that describes the boundaries of polygon
      */
    def vertexes: Seq[Point]

    /** The center of the polygon
      * @return
      *   the center of the polygon
      */
    def center: Point

    /** The area of the polygon
      * @return
      *   the area of the polygon
      */
    def area: Double

    /** Tests if the given point is inside the boundaries of the Polygon.
      * @param point
      *   the point to be tested
      * @return
      *   true if the specified point is inside the Polygon boundary; false
      *   otherwise.
      */
    def contains(point: Point): Boolean

  type Polygon2D = Polygon[Point2D]

  /** Factory for [[Polygon2D]] instance in a two dimensional space.
    */
  object Polygon2D:
    /** Create a Polygon2D from the given vertexes
      * @param vertexes
      *   the vertexes of the Polygon
      * @return
      *   a new Polygon2D instance described by the given vertexes
      */
    def apply(vertexes: Seq[Point2D]): Polygon2D = JavaAwtPolygon2D(vertexes)

    private case class JavaAwtPolygon2D(vertexes: Seq[Point2D])
        extends Polygon2D:
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

  /** Represent a collection of Polygon with generic definition of Point.
    *
    * @tparam Point
    *   the type of point that describes the polygon
    */
  trait MultiPolygon[Point]:
    def polygons: Seq[Polygon[Point]]
    def area: Double
    def contains(point: Point): Boolean
    def split(splitsNumber: Int): Seq[MultiPolygon[Point]]

  type MultiPolygon2D = MultiPolygon[Point2D]

  /** Factory for [[MultiPolygon2D]] instance in a two dimensional space.
    */
  object MultiPolygon2D:
    /** Create a MultiPolygon2D from the given sequence of Polygon
      * @param polygons
      *   the polygons that compose the MultiPolygon
      * @return
      *   a new MultiPolygon2D instance composed by the given polygons
      */
    def apply(polygons: Seq[Polygon2D]): MultiPolygon2D =
      MultiPolygon2DImpl(polygons)

    /** Create a MultiPolygon2D from the given Polygon
      * @param polygon
      *   the polygon that compose the MultiPolygon
      * @return
      *   a new MultiPolygon2D instance composed by a single polygon
      */
    def apply(polygon: Polygon2D): MultiPolygon2D =
      MultiPolygon2DImpl(Seq(polygon))
    private case class MultiPolygon2DImpl(
      override val polygons: Seq[Polygon2D]
    ) extends MultiPolygon2D:

      override def area: Double = ???

      override def contains(point: Point2D): Boolean = ???

      override def split(splitsNumber: Int): Seq[MultiPolygon[Point2D]] = ???
