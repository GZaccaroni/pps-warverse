package it.unibo.warverse.domain.engine.simulation

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

class SimulationEngineTest() extends AnyFunSuite with Matchers
/*
  test("A disposable should not invoke its dispose method if not disposed") {
    val disposableCalled = AtomicBoolean(false)
    val disposable = Disposable(() => disposableCalled := true)

    disposableCalled.get() mustBe false
  }

  test("A disposable should invoke its dispose method when being disposed") {
    val disposableCalled = AtomicBoolean(false)
    val disposable = Disposable(() => disposableCalled := true)
    disposable.dispose()

    disposableCalled.get() mustBe true
  }
 */
