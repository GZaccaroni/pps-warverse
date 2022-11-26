package it.unibo.warverse.domain.model.fight

object SimulationEvent:
  sealed abstract class SimulationEvent

  class AttackEvent(target: Fight.Attackable)
