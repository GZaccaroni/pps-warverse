package it.unibo.warverse.domain.engine.components

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.common.Geometry.Point2D
import it.unibo.warverse.domain.model.common.Listen.*
import it.unibo.warverse.domain.model.fight.Fight.AttackAction
import it.unibo.warverse.domain.model.fight.SimulationEvent
import it.unibo.warverse.domain.model.fight.TargetFinderStrategy.TargetFinderStrategy2D
import it.unibo.warverse.domain.model.world.World.Country
import monix.eval.Task

import scala.annotation.tailrec
import scala.collection.immutable.{AbstractSeq, LinearSeq}

class AttackSimulationComponent
    extends SimpleListenable[SimulationEvent]
    with SimulationComponent:
  override def run(using environment: Environment): Task[Environment] =
    Task {
      given TargetFinderStrategy2D = TargetFinderStrategy2D()

      val events = for
        country <- environment.countries
        unit <- country.armyUnits
        event <- unit.attack()
      yield event
      updateEnvironment(environment, events)
    }

  @tailrec
  private def updateEnvironment(
    environment: Environment,
    events: Seq[AttackAction]
  ): Environment =
    events match
      case (event: AttackAction.AreaAttackAction) :: tail =>
        val countryOption =
          environment.countries.find(_.boundaries.contains(event.target))

        val newCountry = countryOption.map { country =>
          val density = country.citizens / country.boundaries.area
          val citizens =
            country.citizens - (density * event.areaOfImpact).toInt
          country.copy(citizens = citizens)
        }
        updateEnvironment(
          newCountry
            .map(environment.replacingCountry)
            .getOrElse(environment),
          tail
        )
      case (event: AttackAction.PrecisionAttackAction) :: tail =>
        updateEnvironment(
          environment.copiedWith(
            environment.countries.map(country =>
              country.copy(
                armyUnits = country.armyUnits.filter(_.position == event.target)
              )
            )
          ),
          tail
        )
      case Seq() => environment
