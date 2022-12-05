package it.unibo.warverse.domain.model.fight

import it.unibo.warverse.domain.model.common.Geometry.Point2D

object SimulationEvent:
  sealed abstract class SimulationEvent

  /** Represent an attack event
    */
  trait AttackEvent

  /** Case class that represent an AttackEvent on a specified area
    * @param target
    *   the position of the attack
    * @param areaOfImpact
    *   the area involved in the attack
    */
  case class AreaAttackEvent(target: Point2D, areaOfImpact: Double)
      extends AttackEvent

  /** Case class that represent an AttackEvent on a target unit
    * @param target
    *   the position of the attack
    */
  case class PrecisionAttackEvent(target: Point2D) extends AttackEvent
