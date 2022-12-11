package it.unibo.warverse.domain.engine.prolog

import it.unibo.warverse.domain.model.common.Geometry.{Point, Point2D}
import it.unibo.warverse.domain.engine.prolog.PrologEngine.*
import alice.tuprolog.{Prolog, SolveInfo, Struct, Term, Theory, Var}
import it.unibo.warverse.domain.engine.prolog.PrologEngine
import it.unibo.warverse.domain.engine.prolog.PrologEngine.given_Conversion_String_Term

import scala.jdk.CollectionConverters.*

object PrologPredicates:
  private val engine = PrologEngine()

  def distanceBetween[PointType <: Point[PointType]](
    pointA: Point[PointType],
    pointB: Point[PointType]
  ): Double =
    val resultVar = Var("DIST")
    val res = engine
      .getResultVars(
        Struct(
          "distance",
          pointA.coordinates.toPrologList,
          pointB.coordinates.toPrologList,
          resultVar
        )
      )
    res.headOption
      .flatMap(head => head.find(_.getName == resultVar.getName))
      .map(_.getTerm.toString.toDouble)
      .getOrElse(0.0)
