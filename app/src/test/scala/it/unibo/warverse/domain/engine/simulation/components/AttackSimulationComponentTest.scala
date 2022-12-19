package it.unibo.warverse.domain.engine.simulation.components

import org.scalatest.matchers.must.Matchers
import it.unibo.warverse.domain.model.common.DomainExample
import it.unibo.warverse.domain.model.world.Relations.*
import monix.execution.Scheduler.Implicits.global
import org.scalatest.funsuite.AsyncFunSuite
import monix.testing.scalatest.MonixTaskTest
import it.unibo.warverse.domain.model.Environment

class AttackSimulationComponentTest
    extends AsyncFunSuite
    with MonixTaskTest
    with Matchers:
  private val component = AttackSimulationComponent()
  private val initialEnv = DomainExample.environment
    .copiedWith(
      DomainExample.environment.countries.map(country =>
        country.addingResources(80)
      )
    )

  test("From starting position only A must lose a unit") {
    component
      .run(initialEnv)
      .asserting(resultEnv =>
        resultEnv.countries.find(_.id == "ID_A").get.armyUnits.size mustBe 2
        resultEnv.countries.find(_.id == "ID_B").get.armyUnits.size mustBe 3
        resultEnv.countries.find(_.id == "ID_C").get.armyUnits.size mustBe 0
      )
  }
