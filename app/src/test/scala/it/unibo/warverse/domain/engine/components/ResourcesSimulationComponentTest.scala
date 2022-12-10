package it.unibo.warverse.domain.engine.components

import org.scalatest.matchers.must.Matchers
import it.unibo.warverse.domain.model.common.DomainExample
import it.unibo.warverse.domain.model.world.Relations.*
import monix.execution.Scheduler.Implicits.global
import org.scalatest.funsuite.AsyncFunSuite
import monix.testing.scalatest.MonixTaskTest
import it.unibo.warverse.domain.model.fight.ArmyTest.given_Environment
import it.unibo.warverse.domain.model.Environment

class ResourcesSimulationComponentTest
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
  given Environment = environment
  ResourcesSimulationComponent().run.foreach(env => this.environment = env)

  test("Updating resources of all states") {
    environment.countries.find(_.id == "ID_A").get.resources mustBe 18
    environment.countries.find(_.id == "ID_B").get.resources mustBe 0
    environment.countries.find(_.id == "ID_C").get.resources mustBe 100
  }