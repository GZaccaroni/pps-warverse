package it.unibo.warverse.domain.model.fight

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.common.Life
import it.unibo.warverse.domain.model.common.Geometry.Point2D
import it.unibo.warverse.domain.model.common.Math.Percentage
import it.unibo.warverse.domain.model.common.Movement.Movable
import it.unibo.warverse.domain.model.fight.Fight
import it.unibo.warverse.domain.model.fight.TargetFinderStrategy
import it.unibo.warverse.domain.model.world.World

import scala.util.Random

object Army:
  /** Represent an entity with periodic consume of resources. */
  trait ResourcesConsumer:
    /** The quantity of resources that consume every day
      * @return
      *   a representation of the daily resource consume
      */
    def dailyConsume: Life.Resources

  /** The ArmyUnit base trait. It can attack, be moved and consume resources.
    *
    * Extends [[Fight.Attacker]], [[Movable]] and [[ResourcesConsumer]]
    */
  trait ArmyUnit extends Fight.Attacker, Movable[ArmyUnit], ResourcesConsumer:
    override type Position = Point2D

    /** The name of the army unit
      * @return
      *   the name of the army unit
      */
    def name: String

    /** The identifier of the country that unit belongs
      * @return
      *   the id of the country that unit belongs
      */
    def countryId: World.CountryId

    /** The army unit's attack type
      * @return
      *   the army unit's attack type
      */
    def attackType: Fight.AttackType

    /** The speed of army unit in movements
      * @return
      *   the speed of army unit in movements
      */
    def speed: Double

    /** The probability of a single hit to be successfully, expressed in
      * [[Percentage]]
      *
      * @return
      *   the probability of a single hit to be successfully
      */
    def chanceOfHit: Percentage

    /** The maximum distance an attack target can't be hit beyond
      * @return
      *   the maximum distance an attack target can't be hit beyond
      */
    def rangeOfHit: Double

    /** The number of available hits for each attack
      * @return
      *   the number of available hits
      */
    def availableHits: Int

    /** A copy of the army unit with given position
      * @param position
      *   the new position of the unit
      * @return
      *   A new ArmyUnit which is the copy of this with given position
      */
    protected def copied(position: Position): ArmyUnit

    override def moved()(using
      environment: Environment,
      strategy: TargetFinderStrategy[Position]
    ): ArmyUnit =
      val potentialTargets = strategy.findTargets(countryId, attackType)
      val nearestTarget = potentialTargets.minByOption(_.distanceFrom(position))
      val targetPosition = nearestTarget.getOrElse(
        environment.countries
          .find(_.id == countryId)
          .map(_.boundaries.center)
          .getOrElse(position)
      )

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
    override def attack()(using
      strategy: TargetFinderStrategy[Position]
    ): Seq[SimulationEvent.AttackEvent] =
      val availableTargets = strategy
        .findTargets(countryId, attackType)
        .filter(target => position.distanceFrom(target) < rangeOfHit)
        .sortBy(_.distanceFrom(position))
      for
        target <- availableTargets.take(availableHits)
        probabilityOfSuccess <- Seq.fill(availableHits)(Random.nextInt(100))
        if probabilityOfSuccess < chanceOfHit
      yield SimulationEvent.PrecisionAttackEvent(target)

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
  ) extends ArmyUnit:
    override def attackType: Fight.AttackType = Fight.AttackType.Area
    override def copied(position: Point2D): ArmyUnit = copy(position = position)
    override def attack()(using
      strategy: TargetFinderStrategy[Position]
    ): Seq[SimulationEvent.AttackEvent] =
      val availableTargets = strategy
        .findTargets(countryId, attackType)
        .filter(target => position.distanceFrom(target) < rangeOfHit)
        .sortBy(_.distanceFrom(position))
      for
        (target, _) <- availableTargets.zip(0 until availableHits)
        probabilityOfSuccess <- Seq.fill(availableHits)(Random.nextInt(100))
        if probabilityOfSuccess < chanceOfHit
      yield SimulationEvent.AreaAttackEvent(target, areaOfImpact)
