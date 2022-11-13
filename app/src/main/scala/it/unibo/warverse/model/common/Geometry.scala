package it.unibo.warverse.model.common

object Geometry:
  case class Point2D(x: Double, y: Double)

  trait Polygon[Vertex](vertexes: List[Vertex])

  case class Polygon2D(vertexes: List[Point2D])
      extends Polygon[Point2D](vertexes)
