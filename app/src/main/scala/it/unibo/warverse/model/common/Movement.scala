package it.unibo.warverse.model.common

import it.unibo.warverse.model.common.Geometry.Point2D

object Movement:
  trait Locatable:
    type Position
    def position: Position

  trait Movable[Entity <: Movable[Entity]] extends Locatable:
    def copy(position: Position): Entity

  trait Movable2D[Entity <: Movable2D[Entity]] extends Movable[Entity]:
    override type Position = Point2D
