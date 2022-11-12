package it.unibo.warverse.model.common

object Movement:
  trait Locatable:
    type Position
    def position: Position

  trait Movable extends Locatable:
    def speed: Double
