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
    extends AsyncFunSuite
    with MonixTaskTest
    with Matchers:
  val environment: Environment = DomainExample.environment
  val newEnv: Task[Environment] =
    RelationsSimulationComponent().run(environment)

  test("Initial relations between A and C must be neutral") {
    environment.interCountryRelations.relationStatus(
      "ID_A",
      "ID_C"
    ) mustBe empty
  }

  test("Relation between A and C after the update must be war") {
    newEnv.asserting(env =>
      env.interCountryRelations
        .relationStatus("ID_A", "ID_C")
        .head mustBe RelationStatus.WAR
    )
  }
