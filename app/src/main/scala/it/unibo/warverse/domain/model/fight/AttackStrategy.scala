package it.unibo.warverse.domain.model.fight

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.fight.Fight
import it.unibo.warverse.domain.model.common.Geometry

trait AttackStrategy:
  type AttackPosition

  def attackTargets(
    attackType: Fight.AttackType
  ): Seq[AttackPosition]

object AttackStrategy:
  def attackStrategy2D(environment: Environment): AttackStrategy2D =
    ???

  trait AttackStrategy2D extends AttackStrategy:
    override type AttackPosition = Geometry.Point2D
