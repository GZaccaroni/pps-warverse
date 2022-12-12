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
    val result =
      for
        initialCountry <- initialEnv.countries
        currentCountry <- environment.countries
        if initialCountry.id == currentCountry.id
        army <- initialCountry.armyUnits
        army2 <- currentCountry.armyUnits
        pos1 = army.position
        pos2 = army2.position
      yield (pos1.x != pos2.x || pos1.y != pos2.y)
    result.forall(_ == true) must be(true)
  }
