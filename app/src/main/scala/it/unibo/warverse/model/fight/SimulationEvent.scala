package it.unibo.warverse.model.fight

object SimulationEvent:
  sealed abstract class SimulationEvent

  class AttackEvent(target: Fight.Attackable)
