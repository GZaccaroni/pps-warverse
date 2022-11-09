package it.unibo.warverse.model.army

import it.unibo.warverse.model.common.Geometry.Point2D
import it.unibo.warverse.model.common.Math.Percentage
import it.unibo.warverse.model.common.Movement.Movable

object ArmyUnitModule:
  trait Attacker:
    def chanceOfHit: Percentage
    def rangeOfHit: Double
    def availableHits: Int

  trait ResourcesConsumer:
    def dailyConsume: Double

  trait ArmyUnit extends Attacker, Movable, ResourcesConsumer:
    override type Position = Point2D
    def name: String

  case class PrecisionArmyUnit(
      override val name: String,
      override val chanceOfHit: Percentage,
      override val rangeOfHit: Double,
      override val availableHits: Int,
      override val dailyConsume: Double,
      override val speed: Double,
      override val position: Point2D
  ) extends ArmyUnit

  case class AreaArmyUnit(
      override val name: String,
      override val chanceOfHit: Percentage,
      override val rangeOfHit: Double,
      override val availableHits: Int,
      override val dailyConsume: Double,
      override val speed: Double,
      override val position: Point2D,
      areaOfHit: Double
  ) extends ArmyUnit
