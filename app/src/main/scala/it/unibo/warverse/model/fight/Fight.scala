package it.unibo.warverse.model.fight

import it.unibo.warverse.model.common.Life
import it.unibo.warverse.model.common.Math.Percentage
import it.unibo.warverse.model.common.Movement.Movable

object Fight:
  trait Attacker[Entity <: Attacker[Entity]]:

    def chanceOfHit: Percentage
    def rangeOfHit: Double
    def availableHits: Int

    def attack(
      availableTargets: List[Attackable[?]]
    ): List[Attackable[?]]
  trait Attackable
  trait AttackableUnit extends Attackable, Locatable:
    override type Position = Geometry.Point2D
  trait AttackableArea extends Attackable, Locatable:
    override type Position = Geometry.Polygon2D

  trait AttackerWithAreaImpact[Entity <: Attacker[Entity]]
      extends Attacker[Entity]:
    def areaOfImpact: Double
  enum AttackType:
    case Area
    case Precision
