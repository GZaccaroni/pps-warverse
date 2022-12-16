package it.unibo.warverse.domain.model.common

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.fight.TargetFinderStrategy.TargetFinderStrategy

object Movement:
  /** Identifies an object that can be Located
    */
  trait Locatable:
    type Position

    /** Returns the position of the object
      * @return
      *   position
      */
    def position: Position

  /** Identifies an entity that can be moved
    * @tparam Entity
    *   the entity that can be moved
    */
  trait Movable[Entity <: Movable[Entity]] extends Locatable:
    /** Moves the entity according to a strategy in a given environment
      * @param environment
      *   the environment in which the entity is moving
      * @param strategy
      *   the strategy according to which the entity should move
      * @return
      *   the entity moved to the new location
      */
    def moved()(using
      environment: Environment,
      strategy: TargetFinderStrategy[Position]
    ): Entity
