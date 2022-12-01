package it.unibo.warverse.domain.model.fight

import it.unibo.warverse.domain.model.common.Movement.Locatable
import it.unibo.warverse.domain.model.common.{Geometry, Life}
import it.unibo.warverse.domain.model.fight.SimulationEvent.AttackEvent
import it.unibo.warverse.domain.model.fight.AttackStrategy.AttackStrategy2D

object Fight:
  trait Attacker:
    def attack(
      strategy: AttackStrategy2D
    ): List[AttackEvent]
  trait Attackable
  trait AttackableUnit extends Attackable, Locatable:
    override type Position = Geometry.Point2D
  trait AttackableArea extends Attackable, Locatable:
    override type Position = Geometry.Polygon2D

  enum AttackType:
    case Area
    case Precision
