package it.unibo.warverse.domain.model.common

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.common.Geometry.{Point2D, Polygon2D}
import it.unibo.warverse.domain.model.fight.Army.AreaArmyUnit
import it.unibo.warverse.domain.model.world.Relations.{
  InterstateRelations,
  RelationStatus
}
import it.unibo.warverse.domain.model.world.World.Country

object DomainExample:
  val countryA: Country =
    Country(
      "ID_1",
      "A",
      40,
      Seq.empty,
      0.0,
      Polygon2D(Seq(Point2D(0, 0), Point2D(0, 3), Point2D(3, 3), Point2D(3, 0)))
    )
  val countryB: Country =
    Country(
      "ID_2",
      "B",
      10,
      Seq.empty,
      0.0,
      Polygon2D(Seq(Point2D(3, 0), Point2D(3, 3), Point2D(6, 3), Point2D(6, 0)))
    )
  val countryC: Country =
    Country(
      "ID_3",
      "C",
      20,
      Seq.empty,
      0.0,
      Polygon2D(Seq(Point2D(6, 0), Point2D(6, 3), Point2D(9, 3), Point2D(9, 0)))
    )

  val interstateRelations: InterstateRelations = InterstateRelations(
    Seq(
      ((countryA.id, countryB.id), RelationStatus.WAR),
      ((countryB.id, countryC.id), RelationStatus.ALLIANCE)
    )
  )

  val environment: Environment = Environment(
    Seq(countryA, countryB, countryC),
    interstateRelations,
    0
  )
