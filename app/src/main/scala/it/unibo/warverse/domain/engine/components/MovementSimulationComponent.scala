package it.unibo.warverse.domain.engine.components

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.fight.TargetFinderStrategy.TargetFinderStrategy2D

class MovementSimulationComponent extends SimulationComponent:
  override def run(using environment: Environment): Environment =
    given TargetFinderStrategy2D = TargetFinderStrategy2D()
    environment
      .copiedWith(
        environment.countries.map(country =>
          country.copy(armyUnits = country.armyUnits.map(_.moved()))
        )
      )
