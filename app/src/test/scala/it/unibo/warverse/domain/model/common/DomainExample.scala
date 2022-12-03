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
  val successfullySoldier: AreaArmyUnit =
    AreaArmyUnit(
      "ID_A",
      "soldier",
      Point2D(3, 1.5),
      chanceOfHit = 100,
      rangeOfHit = 2,
      availableHits = 1,
      areaOfImpact = 0.5,
      dailyConsume = 100,
      speed = 0.1
    )
  val failingSoldier: AreaArmyUnit =
    AreaArmyUnit(
      "ID_A",
      "soldier",
      Point2D(4, 1.5),
      chanceOfHit = 0,
      rangeOfHit = 1,
      availableHits = 3,
      areaOfImpact = 0.5,
      dailyConsume = 1,
      speed = 0.2
    )
  val distantSoldier: AreaArmyUnit =
    AreaArmyUnit(
      "ID_A",
      "soldier",
      Point2D(1.5, 1.5),
      chanceOfHit = 100,
      rangeOfHit = 1,
      availableHits = 3,
      areaOfImpact = 0.5,
      dailyConsume = 1,
      speed = 0.05
    )
  val countryA: Country =
    Country(
      "ID_1",
      "A",
      40,
      Seq(successfullySoldier, failingSoldier, distantSoldier),
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
