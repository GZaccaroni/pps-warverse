package it.unibo.warverse.domain.engine.simulation.components

import org.scalatest.matchers.must.Matchers
import it.unibo.warverse.domain.model.common.DomainExample
import it.unibo.warverse.domain.model.world.Relations.*
import monix.execution.Scheduler.Implicits.global
import org.scalatest.funsuite.AsyncFunSuite
import monix.testing.scalatest.MonixTaskTest
import it.unibo.warverse.domain.model.Environment

abstract class SimulationComponentTest[T <: SimulationComponent]
    extends AsyncFunSuite
    with MonixTaskTest
    with Matchers:
  protected val component: T
  protected val initialEnv: Environment

  test("Component should never throw an exception") {
    component
      .run(initialEnv)
      .assertNoException
  }
