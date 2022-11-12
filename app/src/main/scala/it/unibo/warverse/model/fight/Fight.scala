package it.unibo.warverse.model.fight

import it.unibo.warverse.model.common.Life
import it.unibo.warverse.model.common.Math.Percentage

object Fight:

  trait Attackable extends Life.LivingEntity:
    def kill(): Void

  trait Attacker:

    def chanceOfHit: Percentage
    def rangeOfHit: Double
    def availableHits: Int

    def attack(
      availableTargets: List[Attackable]
    ): List[Attackable]

  trait AttackerWithAreaImpact extends Attacker:
    def areaOfImpact: Double
