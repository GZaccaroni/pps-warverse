package it.unibo.warverse.domain.engine.simulation.components

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.fight.TargetFinderStrategy.TargetFinderStrategy2D
import it.unibo.warverse.domain.model.common.Listen.*
import it.unibo.warverse.domain.model.fight.SimulationEvent
import monix.eval.Task

/** Simulates army units movement and updates simulation environment accordingly
  */
class MovementSimulationComponent
    extends SimpleListenable[SimulationEvent]
    with SimulationComponent:
  override def run(environment: Environment): Task[Environment] =
    Task {
      given Environment = environment
      given TargetFinderStrategy2D = TargetFinderStrategy2D()
      environment
        .copiedWith(
          environment.countries.map(country =>
            country.copy(armyUnits = country.armyUnits.map(_.moved()))
          )
        )
    }
