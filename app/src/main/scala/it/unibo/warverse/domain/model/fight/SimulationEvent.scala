package it.unibo.warverse.domain.model.fight

import it.unibo.warverse.domain.model.common.Geometry.Point2D

object SimulationEvent:
  sealed abstract class SimulationEvent

  trait AttackEvent

  case class AreaAttackEvent(target: Point2D, areaOfImpact: Double)
      extends AttackEvent

  case class PrecisionAttackEvent(target: Point2D) extends AttackEvent
