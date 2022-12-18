package it.unibo.warverse.domain.model.common

import it.unibo.warverse.domain.engine.prolog.{PrologEngine, PrologPredicates}
import it.unibo.warverse.domain.model.common.Math.DoubleWithAlmostEquals
import java.awt.geom as AwtGeom
import scala.math.{min, *}
import scala.annotation.tailrec

object Geometry:
  /** Represents an n-dimensional Point
    * @tparam PointType
    *   the actual type of the Point
    */
  trait Point[PointType <: Point[PointType]](val coordinates: Seq[Double]):
    /** It computes the distance between this and a point given by param.
      *
      * @param point
      *   the point from which calculate the distance
      * @return
      *   the distance between the two points
      */
    def distanceFrom(point: PointType): Double =
      PrologPredicates.distanceBetween(this, point)

    override def equals(obj: Any): Boolean =
      given Math.Precision(0.001)
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
      extends Point[Point2D](Seq(x, y)),
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
      *   true if the polygon contains the [[Point]] else false
      */
    def contains(point: Point): Boolean

  object Polygon:
    /** Builds a 2-Dimensional polygon
      * @param vertexes
      *   vertexes of the polygon
      * @return
      *   a [[Polygon2D]]
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

      /** Compute the area with the Sohelace formula
        *
        * @return
        *   the area of Polygon2D
        */
      override def area: Double =
        val (xCoordinates, yCoordinates) =
          vertexes.foldRight((Seq[Double](), Seq[Double]()))((a, b) =>
            (a.x +: b._1, a.y +: b._2)
          )
        (for
          i <- vertexes.indices
          prev = (i - 1 + vertexes.length) % vertexes.length
          area = (yCoordinates(prev) + yCoordinates(i)) * (xCoordinates(
            prev
          ) - xCoordinates(i))
        yield area).sum.abs / 2

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

  /** Factory for [[MultiPolygon]] instance in a two dimensional space.
    */
  object MultiPolygon:
    /** Create a MultiPolygon2D from the given sequence of Polygon
      * @param polygons
      *   the polygons that compose the MultiPolygon
      * @return
      *   a new MultiPolygon2D instance composed by the given polygons
      */
    def apply(polygons: Seq[Polygon[Point2D]]): MultiPolygon[Point2D] =
      MultiPolygon2DImpl(polygons)

    /** Create a MultiPolygon2D from the given Polygon
      * @param polygon
      *   the polygon that compose the MultiPolygon
      * @return
      *   a new MultiPolygon2D instance composed by a single polygon
      */
    def apply(polygon: Polygon[Point2D]): MultiPolygon[Point2D] =
      MultiPolygon(Seq(polygon))

    private case class MultiPolygon2DImpl(
      override val polygons: Seq[Polygon[Point2D]]
    ) extends MultiPolygon[Point2D]:

      override def area: Double = polygons.map(_.area).sum

      override def contains(point: Point2D): Boolean =
        polygons.exists(_.contains(point))

      override def split(splitsNumber: Int): Seq[MultiPolygon[Point2D]] =
        @tailrec
        def splitPolygon(
          polygons: Seq[Polygon[Point2D]],
          splits: Int
        ): Seq[MultiPolygon[Point2D]] =
          if polygons.length >= splits then
            polygons
              .grouped((polygons.length.toDouble / splits).ceil.toInt)
              .map(MultiPolygon(_))
              .toSeq
          else splitPolygon(splitLargest(polygons), splits)

        def splitLargest(
          polygons: Seq[Polygon[Point2D]]
        ): Seq[Polygon[Point2D]] =
          val (polygon, polygonIndex) = polygons.zipWithIndex.maxBy(_._1.area)
          // Vertexes reordering to improve multiple split
          val vertexesToSplit = polygon.vertexes.drop(
            polygon.vertexes.length / 2
          ) ++ polygon.vertexes.take(polygon.vertexes.length / 2)
          val cuttingLinePoints = (vertexesToSplit.head, polygon.center)
          val polygonSides =
            vertexesToSplit
              .sliding(2)
              .map(seq => (seq.head, seq(1)))
              .toSeq :+ (vertexesToSplit.last, vertexesToSplit.head)
          val intersectionSide = polygonSides
            .drop(1)
            .find(
              intersection(
                _,
                cuttingLinePoints._1,
                cuttingLinePoints._2
              ).isDefined
            )
            .get
          val intersectionPoint = intersection(
            intersectionSide,
            cuttingLinePoints._1,
            cuttingLinePoints._2
          ).get
          val (subSides1, subSides2) =
            polygonSides.splitAt(
              polygonSides.indexOf(intersectionSide) + 1
            )
          val subPolygon1 = Polygon(subSides1.map(_._1) :+ intersectionPoint)
          val subPolygon2 = Polygon(
            if intersectionPoint != subSides2.head._1 then
              intersectionPoint +: subSides2.head._1 +: subSides2.map(
                _._2
              )
            else intersectionPoint +: subSides2.map(_._2)
          )
          polygons.patch(
            polygonIndex,
            Seq(subPolygon1, subPolygon2),
            1
          )

        /** Compute the point of intersection that belongs to `segment` with the
          * straight passing for points `A` and `B`
          *
          * @param segment
          *   the points that define a segment
          * @param A
          *   the first point that define the straight
          * @param B
          *   the second point that define the straight
          * @return
          *   an option value containing the point of intersection in the
          *   segment if it exists, or None if none exists.
          */
        def intersection(
          segment: (Point2D, Point2D),
          A: Point2D,
          B: Point2D
        ): Option[Point2D] =
          val a1 = segment._2.y - segment._1.y
          val b1 = segment._1.x - segment._2.x
          val c1 = a1 * segment._1.x + b1 * segment._1.y
          val a2 = B.y - A.y
          val b2 = A.x - B.x
          val c2 = a2 * A.x + b2 * A.y
          val determinant = a1 * b2 - a2 * b1
          if determinant != 0 then
            val x = (b2 * c1 - b1 * c2) / determinant
            val y = (a1 * c2 - a2 * c1) / determinant
            val (minX, maxX) =
              (min(segment._1.x, segment._2.x), max(segment._1.x, segment._2.x))
            val (minY, maxY) =
              (min(segment._1.y, segment._2.y), max(segment._1.y, segment._2.y))
            given Math.Precision(0.001)
            if (x > minX || (x ~= minX)) &&
              (x < maxX || (x ~= maxX)) &&
              (y > minY || (y ~= minY)) &&
              (y <= maxY || (y ~= minY))
            then Option(Point2D(x, y))
            else Option.empty
          else Option.empty

        splitPolygon(polygons, splitsNumber)
