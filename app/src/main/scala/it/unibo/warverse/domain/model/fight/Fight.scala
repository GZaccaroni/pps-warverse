package it.unibo.warverse.domain.model.fight

import it.unibo.warverse.domain.model.common.Movement.Locatable
import it.unibo.warverse.domain.model.common.{Geometry, Life}
import it.unibo.warverse.domain.model.fight.SimulationEvent.AttackEvent
import it.unibo.warverse.domain.model.fight.TargetFinderStrategy.TargetFinderStrategy2D

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
    ): Seq[AttackEvent]

  trait Attackable
  trait AttackableUnit extends Attackable, Locatable:
    override type Position = Geometry.Point2D
  trait AttackableArea extends Attackable, Locatable:
    override type Position = Geometry.Polygon2D

  /** Describe different type of attack
    */
  enum AttackType:
    case Area
    case Precision
