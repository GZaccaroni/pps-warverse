package it.unibo.warverse.presentation.controllers

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.common.Geometry.Point2D
import it.unibo.warverse.domain.model.fight.TargetFinderStrategy
import it.unibo.warverse.domain.model.fight.TargetFinderStrategy.TargetFinderStrategy2D
import it.unibo.warverse.domain.model.fight.SimulationEvent.{
  AreaAttackEvent,
  AttackEvent,
  PrecisionAttackEvent
}
import it.unibo.warverse.domain.model.world.World.Country

trait AttackController:
  def attackAndUpdate(environment: Environment): Environment

object AttackController:
  def apply(environment: Environment): AttackController =
    AttackControllerImpl(environment)

  private case class AttackControllerImpl(environment: Environment)
      extends AttackController:

    def attackAndUpdate(environment: Environment): Environment =
      given Environment = environment
      given TargetFinderStrategy2D = TargetFinderStrategy.attackStrategy2D()
      val events = for
        country <- environment.countries
        unit <- country.armyUnits
        event <- unit.attack()
      yield event
      updateEvent(environment, events)

    def updateEvent(
      environment: Environment,
      attackEvents: Seq[AttackEvent]
    ): Environment =
      val areaAttackEvents = attackEvents.collect({
        case event: AreaAttackEvent =>
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
