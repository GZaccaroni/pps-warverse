package it.unibo.warverse.domain.model.fight

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.world.World.CountryId

/** It represents an event that happened during the simulation
  */
sealed trait SimulationEvent
object SimulationEvent:

  /** This event is fired every time an iteration of the simulation is completed
    */
  case class IterationCompleted(environment: Environment)
      extends SimulationEvent

  /** This event is fired when the simulation completes
    */
  case class SimulationCompleted(environment: Environment) extends SimulationEvent

  /** This event is fired when a country wins a war
    */
  case class CountryWonWar(winnerId: CountryId, loserId: CountryId, day: Int)
      extends SimulationEvent
