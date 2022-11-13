package it.unibo.warverse.model.fight

import it.unibo.warverse.model.common.Life
import it.unibo.warverse.model.common.Geometry.Point2D
import it.unibo.warverse.model.common.Math.Percentage
import it.unibo.warverse.model.common.Movement.Movable
import it.unibo.warverse.model.fight.Fight

object Army:
  trait ResourcesConsumer:
    def dailyConsume: Life.Resources

  trait ArmyUnit extends Fight.Attacker, Movable, ResourcesConsumer:
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
  ) extends ArmyUnit:
    override def attack(
      availableTargets: List[Fight.Attackable]
    ): List[Fight.Attackable] = ???

  case class AreaArmyUnit(
    override val name: String,
    override val chanceOfHit: Percentage,
    override val rangeOfHit: Double,
    override val availableHits: Int,
    override val dailyConsume: Double,
    override val speed: Double,
    override val position: Point2D,
    override val areaOfImpact: Double
  ) extends ArmyUnit
      with Fight.AttackerWithAreaImpact:
    override def attack(
      availableTargets: List[Fight.Attackable]
    ): List[Fight.Attackable] = ???
