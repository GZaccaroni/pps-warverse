package it.unibo.warverse.model.fight

import it.unibo.warverse.model.fight.Fight
import it.unibo.warverse.model.common.Geometry
import it.unibo.warverse.model.world.World.WorldState

trait AttackStrategy:
  type AttackPosition

  def attackTargets(
    attackType: Fight.AttackType
  ): List[AttackPosition]

object AttackStrategy:
  def attackStrategy2D(world: WorldState): AttackStrategy2D =
    ???

  trait AttackStrategy2D extends AttackStrategy:
    override type AttackPosition = Geometry.Point2D
