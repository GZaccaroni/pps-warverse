package it.unibo.warverse.domain.model.fight

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.common.Life
import it.unibo.warverse.domain.model.common.Geometry.Point2D
import it.unibo.warverse.domain.model.common.Math.Percentage
import it.unibo.warverse.domain.model.common.Movement.Movable
import it.unibo.warverse.domain.model.fight.Fight
import it.unibo.warverse.domain.model.fight.AttackStrategy.AttackStrategy2D
import it.unibo.warverse.domain.model.world.World

import scala.util.Random

object Army:
  trait ResourcesConsumer:
    def dailyConsume: Life.Resources

  trait ArmyUnit extends Fight.Attacker, Movable[ArmyUnit], ResourcesConsumer:
    override type Position = Point2D
    def name: String
    def countryId: World.CountryId
    def attackType: Fight.AttackType
    def speed: Double
    def chanceOfHit: Percentage
    def rangeOfHit: Double
    def availableHits: Int
    def dailyConsume: Double
    protected def copied(position: Position): ArmyUnit

    override def moved(environment: Environment): ArmyUnit =
      val strategy = AttackStrategy.attackStrategy2D(environment, countryId)
      val potentialTargets = strategy.attackTargets(attackType)
      val nearestTarget = potentialTargets.minByOption(_.distanceFrom(position))
      val targetPosition = nearestTarget match
        case Some(targetPosition) => targetPosition
        case None =>
          environment.countries
            .find(_.id == countryId)
            .map(_.boundaries.center)
            .getOrElse(position)
      copied(
        position = position.moved(toward = targetPosition, of = speed)
      )

  case class PrecisionArmyUnit(
    override val countryId: World.CountryId,
    override val name: String,
    override val position: Point2D,
    chanceOfHit: Percentage,
    rangeOfHit: Double,
    availableHits: Int,
    dailyConsume: Double,
    speed: Double
  ) extends ArmyUnit:
    override def attackType: Fight.AttackType = Fight.AttackType.Precision
    override def copied(position: Point2D): ArmyUnit = copy(position = position)
    override def attack(
      strategy: AttackStrategy2D
    ): Seq[SimulationEvent.AttackEvent] =
      ???

  case class AreaArmyUnit(
    override val countryId: World.CountryId,
    override val name: String,
    override val position: Point2D,
    chanceOfHit: Percentage,
    rangeOfHit: Double,
    availableHits: Int,
    override val dailyConsume: Double,
    speed: Double,
    areaOfImpact: Double
  ) extends ArmyUnit
      with Fight.Attacker:
    override def attackType: Fight.AttackType = Fight.AttackType.Area
    override def copied(position: Point2D): ArmyUnit = copy(position = position)
    override def attack(
      strategy: AttackStrategy2D
    ): Seq[SimulationEvent.AttackEvent] =
      val availableTargets = strategy
        .attackTargets(attackType)
        .filter(target => position.distanceFrom(target) < rangeOfHit)
      for
        (target, _) <- availableTargets.zip(0 until availableHits)
        probabilityOfSuccess <- Seq.fill(availableHits)(Random.nextInt(100))
        if probabilityOfSuccess < chanceOfHit
      yield SimulationEvent.AreaAttackEvent(target, areaOfImpact)
