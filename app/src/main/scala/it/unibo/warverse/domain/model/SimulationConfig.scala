package it.unibo.warverse.domain.model

import it.unibo.warverse.domain.model.world.World.Country
import it.unibo.warverse.domain.model.world.Relations.InterCountryRelations

/** The configuration of the simulation
  * @param countries
  *   a list of countries
  * @param interCountryRelations
  *   relations between countries
  */
case class SimulationConfig(
  countries: Seq[Country],
  interCountryRelations: InterCountryRelations
)
