package it.unibo.warverse.domain.engine.components

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.common.Listen.*
import it.unibo.warverse.domain.model.fight.SimulationEvent
import it.unibo.warverse.domain.model.fight.SimulationEvent.CountryWonWar
import it.unibo.warverse.domain.model.world.Relations.{
  InterstateRelations,
  RelationStatus
}
import it.unibo.warverse.domain.model.world.World.{Country, CountryId}
import it.unibo.warverse.domain.model.fight.SimulationEvent

class WarSimulationComponent
    extends SimpleListenable[SimulationEvent]
    with SimulationComponent:
  override def run(using environment: Environment): Environment =
    getFightingCountries()
      .foldLeft(environment) { (environment, countryInWarId) =>
        environment.countries
          .find(_.id == countryInWarId)
          .filter { countryInWar =>
            countryInWar.armyUnits.size <= 0 || countryInWar.citizens <= 0 || countryInWar.resources <= 0
          }
          .map(assignLostResources)
          .getOrElse(environment)
      }
      .copiedWith(day = environment.day + 1)

  private def getFightingCountries()(using
    environment: Environment
  ): Seq[CountryId] =
    environment.countries
      .filter(country =>
        environment.interstateRelations.countryEnemies(country.id).nonEmpty
      )
      .map(_.id)

  private def assignLostResources(
    countryDefeated: Country
  )(using environment: Environment): Environment =
    val winnersId =
      environment.interstateRelations.countryEnemies(countryDefeated.id).toSeq
    val loserResources = countryDefeated.resources
    val loserCitizens = countryDefeated.citizens
    val loserArmy = countryDefeated.armyUnits
    val armyUnitsPerWinner = loserArmy.size / winnersId.size
    val citizensPerWinner = loserCitizens / winnersId.size
    val resourcesPerWinner = loserResources / winnersId.size
    val envWithoutDefeatedCountry =
      environment
        .copiedWith(
          countries = environment.countries.filterNot(_ == countryDefeated),
          interstateRelations = removeLostStateRelation(
            countryDefeated,
            environment.interstateRelations
          )
        )
    winnersId.zipWithIndex
      .foldLeft(envWithoutDefeatedCountry) {
        case (environment, (winnerId, winnerIndex)) =>
          val currentCountries = environment.countries
          val idToCountry = currentCountries
            .find(country => country.id == winnerId)
            .get
          val index = currentCountries.indexOf(
            idToCountry
          )
          val winnerCountry: Country =
            idToCountry
              .addingResources(
                if loserResources - resourcesPerWinner * (winnerIndex + 1) < resourcesPerWinner
                then loserResources - resourcesPerWinner * winnerIndex
                else resourcesPerWinner
              )
              .addingArmyUnits(
                loserArmy
                  .slice(
                    armyUnitsPerWinner * winnerIndex,
                    armyUnitsPerWinner * (winnerIndex + 1)
                  )
              )
              .addingCitizens(
                if loserCitizens - citizensPerWinner * (winnerIndex + 1) < citizensPerWinner
                then loserCitizens - citizensPerWinner * winnerIndex
                else citizensPerWinner
              )
          emitEvent(
            CountryWonWar(winnerId, countryDefeated.id, environment.day)
          )
          envWithoutDefeatedCountry
            .copiedWith(
              currentCountries.updated(
                index,
                winnerCountry
              )
            )
      }

  private def removeLostStateRelation(
    country: Country,
    relations: InterstateRelations
  ): InterstateRelations =
    var result: InterstateRelations = relations
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
