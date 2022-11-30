package it.unibo.warverse.domain.model.common

import it.unibo.warverse.domain.model.common.Geometry.Point2D
import it.unibo.warverse.domain.model.world.World.WorldState

object Movement:
  trait Locatable:
    type Position
    def position: Position

  trait Movable[Entity <: Movable[Entity]] extends Locatable:
    def moved(world: WorldState): Entity

  trait Movable2D[Entity <: Movable2D[Entity]] extends Movable[Entity]:
    override type Position = Point2D