package it.unibo.warverse.domain.engine.components

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.fight.TargetFinderStrategy.TargetFinderStrategy2D
import it.unibo.warverse.domain.model.fight.{
  SimulationEvent,
  TargetFinderStrategy
}
import it.unibo.warverse.domain.model.world.World.Country

import scala.annotation.tailrec

class ResourcesSimulationComponent extends SimulationComponent:
  override def run(using environment: Environment): Environment =
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

  private def isInWar(country: Country)(using
    environment: Environment
  ): Boolean =
    environment.interstateRelations.countryEnemies(country.id).nonEmpty
