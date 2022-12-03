package it.unibo.warverse.domain.model

import it.unibo.warverse.domain.model.world.World.Country
import it.unibo.warverse.domain.model.fight.Army.ArmyUnit
import it.unibo.warverse.domain.model.world.Relations.InterstateRelations
case class SimulationConfig(
  countries: Seq[Country],
  interstateRelations: InterstateRelations
)
