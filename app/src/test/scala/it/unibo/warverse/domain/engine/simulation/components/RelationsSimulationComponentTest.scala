package it.unibo.warverse.domain.engine.simulation.components

import it.unibo.warverse.domain.model.Environment
import org.scalatest.matchers.must.Matchers
import it.unibo.warverse.domain.model.common.DomainExample
import it.unibo.warverse.domain.model.world.Relations.*
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import org.scalatest.funsuite.AsyncFunSuite
import monix.testing.scalatest.MonixTaskTest

class RelationsSimulationComponentTest
    extends SimulationComponentTest[RelationsSimulationComponent]:
  override protected val component: RelationsSimulationComponent =
    RelationsSimulationComponent()
  override protected val initialEnv: Environment = DomainExample.environment

  test("Initial relations between A and C must be neutral") {
    initialEnv.interCountryRelations.relationStatus(
      "ID_A",
      "ID_C"
    ) mustBe empty
  }

  test("Relation between A and C after the update must be war") {
    component
      .run(initialEnv)
      .asserting(resultEnv =>
        resultEnv.interCountryRelations
          .relationStatus("ID_A", "ID_C")
          .head mustBe RelationStatus.WAR
      )
  }
