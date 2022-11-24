package it.unibo.warverse.model.fight

import it.unibo.warverse.model.common.Life
import it.unibo.warverse.model.common.Math.Percentage
import it.unibo.warverse.model.common.Movement.Movable

object Fight:

  trait Attackable[Entity <: Attackable[Entity]]:
    def attacked(): Entity

  trait Attacker[Entity <: Attacker[Entity]]:

    def chanceOfHit: Percentage
    def rangeOfHit: Double
    def availableHits: Int

    def attack(
      availableTargets: List[Attackable[?]]
    ): List[Attackable[?]]

  trait AttackerWithAreaImpact[Entity <: Attacker[Entity]]
      extends Attacker[Entity]:
    def areaOfImpact: Double
  enum AttackType:
    case Area
    case Precision
