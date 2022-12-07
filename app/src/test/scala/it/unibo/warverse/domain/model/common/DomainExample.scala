package it.unibo.warverse.domain.model.common

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.common.Geometry.{Point2D, Polygon2D}
import it.unibo.warverse.domain.model.fight.Army.{
  AreaArmyUnit,
  PrecisionArmyUnit
}
import it.unibo.warverse.domain.model.world.Relations.{
  InterCountryRelations,
  RelationStatus
}
import it.unibo.warverse.domain.model.world.World.Country

object DomainExample:

  val countryAId = "ID_A"
  val countryBId = "ID_B"
  val countryCId = "ID_C"
  object Army:
    object AreaArmyUnits:
      def successfulUnit: AreaArmyUnit =
        AreaArmyUnit(
          countryAId,
          "catapult",
          Point2D(3, 1.5),
          chanceOfHit = 100,
          rangeOfHit = 2,
          availableHits = 1,
          areaOfImpact = 0.5,
          dailyConsume = 100,
          speed = 0.1
        )
      def failingUnit: AreaArmyUnit =
        AreaArmyUnit(
          countryAId,
          "catapult",
          Point2D(4, 1.5),
          chanceOfHit = 0,
          rangeOfHit = 1,
          availableHits = 3,
          areaOfImpact = 0.5,
          dailyConsume = 1,
          speed = 0.2
        )
      def distantUnit: AreaArmyUnit =
        AreaArmyUnit(
          countryAId,
          "catapult",
          Point2D(1.5, 1.5),
          chanceOfHit = 100,
          rangeOfHit = 1,
          availableHits = 3,
          areaOfImpact = 0.5,
          dailyConsume = 1,
          speed = 0.05
        )
    object PrecisionArmyUnits:
      def successfulUnit =
        PrecisionArmyUnit(
          countryBId,
          "soldier",
          Point2D(3.5, 1.5),
          chanceOfHit = 100,
          rangeOfHit = 2,
          availableHits = 1,
          dailyConsume = 100,
          speed = 0.1
        )
      def failingUnit =
        PrecisionArmyUnit(
          countryBId,
          "soldier",
          Point2D(3.5, 1.5),
          chanceOfHit = 0,
          rangeOfHit = 1,
          availableHits = 3,
          dailyConsume = 1,
          speed = 0.2
        )
      def distantUnit =
        PrecisionArmyUnit(
          countryBId,
          "soldier",
          Point2D(8, 8),
          chanceOfHit = 100,
          rangeOfHit = 1,
          availableHits = 3,
          dailyConsume = 1,
          speed = 0.05
        )
  def countryA: Country =
    Country(
      countryAId,
      "A",
      40,
      Seq(
        Army.AreaArmyUnits.successfulUnit,
        Army.AreaArmyUnits.failingUnit,
        Army.AreaArmyUnits.distantUnit
      ),
      0.0,
      Polygon2D(Seq(Point2D(0, 0), Point2D(0, 3), Point2D(3, 3), Point2D(3, 0)))
    )
  def countryB: Country =
    Country(
      countryBId,
      "B",
      10,
      Seq(
        Army.PrecisionArmyUnits.successfulUnit,
        Army.PrecisionArmyUnits.failingUnit,
        Army.PrecisionArmyUnits.distantUnit
      ),
      0.0,
      Polygon2D(Seq(Point2D(3, 0), Point2D(3, 3), Point2D(6, 3), Point2D(6, 0)))
    )
  def countryC: Country =
    Country(
      countryCId,
      "C",
      20,
      Seq.empty,
      0.0,
      Polygon2D(Seq(Point2D(6, 0), Point2D(6, 3), Point2D(9, 3), Point2D(9, 0)))
    )

  val interstateRelations: InterCountryRelations = InterCountryRelations(
    Set(
      ((countryA.id, countryB.id), RelationStatus.WAR),
      ((countryB.id, countryC.id), RelationStatus.ALLIANCE)
    )
  )

  val environment: Environment = Environment(
    Seq(countryA, countryB, countryC),
    interstateRelations,
    0
  )
