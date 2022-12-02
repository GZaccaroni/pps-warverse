package it.unibo.warverse.domain.model.common

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.common.Geometry.Point2D

object Movement:
  trait Locatable:
    type Position
    def position: Position

  trait Movable[Entity <: Movable[Entity]] extends Locatable:
    def moved(environment: Environment): Entity

  trait Movable2D[Entity <: Movable2D[Entity]] extends Movable[Entity]:
    override type Position = Point2D
