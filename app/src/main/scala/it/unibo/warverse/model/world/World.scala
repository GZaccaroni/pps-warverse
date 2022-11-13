package it.unibo.warverse.model.world

import it.unibo.warverse.model.common.{Geometry, Life, Movement}
import it.unibo.warverse.model.fight.Army.ArmyUnit
import it.unibo.warverse.model.fight.Fight

object World:
  case class Citizen(
    position: Geometry.Point2D
  ) extends Movement.Locatable,
        Life.LivingEntity,
        Fight.Attackable:
    override type Position = Geometry.Point2D

    def alive: Boolean = ???

    override def kill(): Void = ???
