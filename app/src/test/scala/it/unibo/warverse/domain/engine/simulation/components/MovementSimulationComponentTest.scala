package it.unibo.warverse.domain.engine.simulation.components

import org.scalatest.matchers.must.Matchers
import it.unibo.warverse.domain.model.common.DomainExample
import it.unibo.warverse.domain.model.world.Relations.*
import monix.execution.Scheduler.Implicits.global
import org.scalatest.funsuite.AsyncFunSuite
import monix.testing.scalatest.MonixTaskTest
import it.unibo.warverse.domain.model.Environment

class MovementSimulationComponentTest
    extends AsyncFunSuite
    with MonixTaskTest
    with Matchers:
  private val component = MovementSimulationComponent()
  private val initialEnv = DomainExample.environment
    .copiedWith(
      DomainExample.environment.countries.map(country =>
        country.addingResources(80)
      )
    )

  test("Initial position must be different from moved position") {
    component
      .run(initialEnv)
      .asserting(resultEnv =>
        val result =
          for
            initialCountry <- initialEnv.countries
            currentCountry <- resultEnv.countries
            if initialCountry.id == currentCountry.id
            army <- initialCountry.armyUnits
            army2 <- currentCountry.armyUnits
            pos1 = army.position
            pos2 = army2.position
          yield pos1.x != pos2.x || pos1.y != pos2.y
        result.forall(_ == true) mustBe true
      )

  }
