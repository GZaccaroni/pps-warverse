package it.unibo.warverse.presentation.controllers

import it.unibo.warverse.domain.model.common.Geometry.Point2D
import it.unibo.warverse.domain.model.fight.AttackStrategy
import it.unibo.warverse.domain.model.fight.SimulationEvent.{
  AreaAttackEvent,
  AttackEvent,
  PrecisionAttackEvent
}
import it.unibo.warverse.domain.model.world.World.{Country, WorldState}

class AttackController:

  def attackAndUpdate(worldState: WorldState): WorldState =
    val events = for
      country <- worldState.countries
      if worldState.interstateRelations.getEnemies(country.id).nonEmpty
      unit <- country.armyUnits
      event <- unit.attack(
        AttackStrategy.attackStrategy2D(worldState, unit.countryId)
      )
    yield event
    updateEvent(worldState, events)

  def updateEvent(
    worldState: WorldState,
    attackEvents: List[AttackEvent]
  ): WorldState =
    val areaAttackEvents = attackEvents.collect({ case event: AreaAttackEvent =>
      event
    })
    val updatedCountries: Iterable[(Country, Country)] = for
      event <- areaAttackEvents
      target = event.target
      areaOfImpact = event.areaOfImpact
      country <- worldState.countries if country.boundaries.center == target
      density = country.citizens / country.boundaries.area
      citizens = country.citizens - (density * areaOfImpact).toInt
      newCountry = country.copy(citizens = citizens)
    yield (country, newCountry)
    worldState.copy(countries =
      worldState.countries
        .diff(updatedCountries.map(_._1).toSeq)
        .concat(updatedCountries.map(_._2))
    )
