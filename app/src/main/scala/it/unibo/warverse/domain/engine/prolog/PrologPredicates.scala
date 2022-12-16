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

  def reachableSortedTargets(
    position: Point2D,
    range: Double,
    targets: Seq[Point2D],
    limit: Int
  ): List[Point2D] =
    val reachableTargetsVar = Var("RT")
    val res = engine
      .getResultVars(
        Struct(
          "reachable_targets_limit",
          position.coordinates.toPrologList,
          range.toString,
          targets.map(_.coordinates.toPrologList).toPrologList,
          limit.toString,
          reachableTargetsVar
        )
      )
    res.headOption
      .flatMap(head => head.find(_.getName == reachableTargetsVar.getName))
      .map(el =>
        mapPrologTermStrToJavaList(el.getTerm.toString)
          .map(
            mapPrologTermStrToJavaList
              .andThen(_.map(_.toDouble))
              .andThen(coords => Point2D(coords.head, coords(1)))
          )
      )
      .getOrElse(List.empty)

  private def mapPrologTermStrToJavaList(termStr: String): List[String] =
    if !isPrologTermStrList(termStr) then
      throw RuntimeException(s"Not a prolog list: $termStr")
    else
      val cleanedStr = termStr.substring(1, termStr.length - 1).trim
      if cleanedStr.isEmpty then List.empty
      else cleanedStr.split(",(?![^\\[]*[\\]])").toList

  private def isPrologTermStrList(termStr: String): Boolean =
    termStr.startsWith("[") && termStr.endsWith("]")
