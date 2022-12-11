package it.unibo.warverse.domain.engine.simulation.components

import org.scalatest.matchers.must.Matchers
import it.unibo.warverse.domain.model.common.DomainExample
import it.unibo.warverse.domain.model.world.Relations.*
import monix.execution.Scheduler.Implicits.global
import org.scalatest.funsuite.AsyncFunSuite
import monix.testing.scalatest.MonixTaskTest
import it.unibo.warverse.domain.model.fight.ArmyTest.given_Environment
import it.unibo.warverse.domain.model.Environment

class MovementSimulationComponentTest
    extends AsyncFunSuite
    with MonixTaskTest
    with Matchers:
  var environment = DomainExample.environment
    .copiedWith(
      DomainExample.environment.countries.map(country =>
        country.addingResources(80)
      )
    )

  RelationsSimulationComponent()
    .run(environment)
    .foreach(env => this.environment = env)

  val initialEnv = environment
  given Environment = environment
  MovementSimulationComponent().run.foreach(env => this.environment = env)

  test("Initial position must be different from moved position") {
    val initialArmy = initialEnv.countries.find(_.id == "ID_A").get.armyUnits.map(unit => unit.position)
    val newArmy = environment.countries.find(_.id == "ID_A").get.armyUnits.map(unit => unit.position)
    assert(initialArmy.apply(0).x != newArmy.apply(0).x)
    assert(initialArmy.apply(1).x != newArmy.apply(1).x)
    assert(initialArmy.apply(2).x != newArmy.apply(2).x)
  }
