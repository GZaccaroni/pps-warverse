package it.unibo.warverse.domain.model.fight

import it.unibo.warverse.domain.model.common.Movement.Locatable
import it.unibo.warverse.domain.model.common.Geometry
import it.unibo.warverse.domain.model.fight.TargetFinderStrategy.TargetFinderStrategy

object Fight:

  /** Represent a entity that can attack */
  trait Attacker:
    /** Type that represents the position of the targets. */
    type Position

    /** A function that compute the events that result of an attack on the
      * available targets given by strategy
      *
      * @param strategy
      *   the strategy used to find available targets
      * @return
      *   the sequence of events result of the attack
      */
    def attack()(using
      strategy: TargetFinderStrategy[Position]
    ): Seq[AttackAction]

  /** Represents an attack action
    */
  sealed trait AttackAction
  object AttackAction:

    /** Case class that represent an AttackAction on a specified area
      *
      * @param target
      *   the position of the attack
      * @param areaOfImpact
      *   the area involved in the attack
      */
    case class AreaAttackAction(target: Geometry.Point2D, areaOfImpact: Double)
        extends AttackAction

    /** Case class that represent an AttackAction on a target unit
      *
      * @param target
      *   the position of the attack
      */
    case class PrecisionAttackAction(target: Geometry.Point2D)
        extends AttackAction

  /** Describe different type of attack
    */
  enum AttackType:
    case Area
    case Precision
