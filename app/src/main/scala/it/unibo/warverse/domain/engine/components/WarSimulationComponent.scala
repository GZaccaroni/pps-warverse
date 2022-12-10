package it.unibo.warverse.domain.engine.components

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.common.Listen.*
import it.unibo.warverse.domain.model.fight.SimulationEvent
import it.unibo.warverse.domain.model.fight.SimulationEvent.CountryWonWar
import it.unibo.warverse.domain.model.world.Relations.{
  InterCountryRelations,
  RelationStatus
}
import it.unibo.warverse.domain.model.world.World.{Country, CountryId}
import monix.eval.Task

/** Simulates war completion and division of assets between winning states
  */
class WarSimulationComponent
    extends SimpleListenable[SimulationEvent]
    with SimulationComponent:
  override def run(environment: Environment): Task[Environment] =
    Task {
      getFightingCountries(environment)
        .foldLeft(environment) { (environment, countryInWarId) =>
          given Environment = environment
          environment.countries
            .find(_.id == countryInWarId)
            .filter { countryInWar =>
              countryInWar.armyUnits.size <= 0 || countryInWar.citizens <= 0 || countryInWar.resources <= 0
            }
            .map(assignLostResources)
            .getOrElse(environment)
        }
        .copiedWith(day = environment.day + 1)
    }

  private def getFightingCountries(
    environment: Environment
  ): Set[CountryId] =
    environment.countries
      .filter(country =>
        environment.interCountryRelations.countryEnemies(country.id).nonEmpty
      )
      .map(_.id)
      .toSet

  private def assignLostResources(
    countryDefeated: Country
  )(using environment: Environment): Environment =
    val winnersId =
      environment.interCountryRelations.countryEnemies(countryDefeated.id).toSeq
    val envWithoutDefeatedCountry =
      environment
        .copiedWith(
          environment.countries.filterNot(_ == countryDefeated),
          removeLostStateRelation(
            countryDefeated,
            environment.interCountryRelations
          )
        )
    if winnersId.nonEmpty then
      val loserResources = countryDefeated.resources
      val loserCitizens = countryDefeated.citizens
      val loserArmy = countryDefeated.armyUnits
      val armyUnitsPerWinner =
        loserArmy.size / winnersId.size
      val citizensPerWinner =
        loserCitizens / winnersId.size
      val resourcesPerWinner =
        loserResources / winnersId.size
      val splittedLoserTerritory =
        countryDefeated.boundaries.split(winnersId.size)
      winnersId.zipWithIndex
        .foldLeft(envWithoutDefeatedCountry) {
          case (envWithoutDefeatedCountry, (winnerId, winnerIndex)) =>
            val currentCountries = envWithoutDefeatedCountry.countries
            emitEvent(
              CountryWonWar(
                winnerId,
                countryDefeated.id,
                envWithoutDefeatedCountry.day
              )
            )
            val idToCountryOption = currentCountries
              .find(_.id == winnerId)

            idToCountryOption match
              case Some(country) =>
                val winnerCountry =
                  country
                    .addingResources(
                      if loserResources - resourcesPerWinner * (winnerIndex + 1) < resourcesPerWinner
                      then loserResources - resourcesPerWinner * winnerIndex
                      else resourcesPerWinner
                    )
                    .addingArmyUnits(
                      loserArmy
                        .slice(
                          armyUnitsPerWinner * winnerIndex,
                          if winnerIndex == winnersId.size - 1 then
                            loserArmy.size
                          else armyUnitsPerWinner * (winnerIndex + 1)
                        )
                        .map(_.copiedWith(countryId = winnerId))
                    )
                    .addingCitizens(
                      if loserCitizens - citizensPerWinner * (winnerIndex + 1) < citizensPerWinner
                      then loserCitizens - citizensPerWinner * winnerIndex
                      else citizensPerWinner
                    )
                    .addingTerritory(splittedLoserTerritory(winnerIndex))
                envWithoutDefeatedCountry
                  .replacingCountry(winnerCountry)
              case None =>
                envWithoutDefeatedCountry

        }
    else envWithoutDefeatedCountry

  private def removeLostStateRelation(
    country: Country,
    relations: InterCountryRelations
  ): InterCountryRelations =
    var result: InterCountryRelations = relations
    val enemies = relations.countryEnemies(country.id)
    val allied = relations.countryAllies(country.id)
    allied.foreach(ally =>
      result =
        result.withoutRelation((country.id, ally), RelationStatus.ALLIANCE)
    )
    enemies.foreach(enemy =>
      result = result.withoutRelation((country.id, enemy), RelationStatus.WAR)
    )
    result
