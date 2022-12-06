package it.unibo.warverse.domain.model.fight

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.world.World.CountryId

sealed trait SimulationEvent
object SimulationEvent:

  case class IterationCompleted(environment: Environment) extends SimulationEvent
  object SimulationCompletedEvent extends SimulationEvent
  case class CountryWinWarEvent(winnerId: CountryId, loserId: CountryId, day: Int) extends SimulationEvent
