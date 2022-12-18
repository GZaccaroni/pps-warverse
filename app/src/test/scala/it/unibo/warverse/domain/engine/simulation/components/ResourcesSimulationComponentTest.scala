package it.unibo.warverse.domain.engine.simulation.components

import org.scalatest.matchers.must.Matchers
import it.unibo.warverse.domain.model.common.DomainExample
import it.unibo.warverse.domain.model.world.Relations.*
import monix.execution.Scheduler.Implicits.global
import org.scalatest.funsuite.AsyncFunSuite
import monix.testing.scalatest.MonixTaskTest
import it.unibo.warverse.domain.model.Environment

class ResourcesSimulationComponentTest
    extends AsyncFunSuite
    with MonixTaskTest
    with Matchers:
  private val component = ResourcesSimulationComponent()
  private val initialEnv = DomainExample.environment
    .copiedWith(
      DomainExample.environment.countries.map(country =>
        country.addingResources(80)
      )
    )

  test("Updating resources of all countries") {
    component
      .run(initialEnv)
      .asserting(resultEnv =>
        resultEnv.countries.find(_.id == "ID_A").get.resources mustBe 18
        resultEnv.countries.find(_.id == "ID_B").get.resources mustBe 0
        resultEnv.countries.find(_.id == "ID_C").get.resources mustBe 100
      )
  }
