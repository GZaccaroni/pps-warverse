package it.unibo.warverse.domain.engine.components

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.fight.TargetFinderStrategy.TargetFinderStrategy2D
import it.unibo.warverse.domain.model.fight.{
  SimulationEvent,
  TargetFinderStrategy
}
import it.unibo.warverse.domain.model.world.World.Country
import it.unibo.warverse.domain.model.common.Listen.*
import scala.annotation.tailrec
import monix.eval.Task

class ResourcesSimulationComponent
    extends SimpleListenable[SimulationEvent]
    with SimulationComponent:
  override def run(using environment: Environment): Task[Environment] =
    Task {
      environment.copiedWith(
        countries = environment.countries
          .map(country =>
            country.addingResources(
              if isInWar(country) then
                country.citizens -
                  country.armyUnits.map(_.dailyConsume).sum
              else country.citizens
            )
          )
      )
    }

  private def isInWar(country: Country)(using
    environment: Environment
  ): Boolean =
    environment.interstateRelations.countryEnemies(country.id).nonEmpty
