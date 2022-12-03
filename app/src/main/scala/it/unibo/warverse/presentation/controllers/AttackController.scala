package it.unibo.warverse.presentation.controllers

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.common.Geometry.Point2D
import it.unibo.warverse.domain.model.fight.AttackStrategy
import it.unibo.warverse.domain.model.fight.SimulationEvent.{
  AreaAttackEvent,
  AttackEvent,
  PrecisionAttackEvent
}
import it.unibo.warverse.domain.model.world.World.Country

class AttackController:

  def attackAndUpdate(environment: Environment): Environment =
    val events = for
      country <- environment.countries
      if environment.interstateRelations.countryEnemies(country.id).nonEmpty
      unit <- country.armyUnits
      event <- unit.attack(
        AttackStrategy.attackStrategy2D(environment, unit.countryId)
      )
    yield event
    updateEvent(environment, events)

  def updateEvent(
    environment: Environment,
    attackEvents: Seq[AttackEvent]
  ): Environment =
    val areaAttackEvents = attackEvents.collect({ case event: AreaAttackEvent =>
      event
    })
    val updatedCountries: Iterable[(Country, Country)] = for
      event <- areaAttackEvents
      target = event.target
      areaOfImpact = event.areaOfImpact
      country <- environment.countries if country.boundaries.center == target
      density = country.citizens / country.boundaries.area
      citizens = country.citizens - (density * areaOfImpact).toInt
      newCountry = country.copy(citizens = citizens)
    yield (country, newCountry)
    environment.copiedWith(countries =
      environment.countries
        .diff(updatedCountries.map(_._1).toSeq)
        .concat(updatedCountries.map(_._2))
    )
