package it.unibo.warverse.domain.engine.simulation

import it.unibo.warverse.domain.model.common.DomainExample
import it.unibo.warverse.domain.model.fight.SimulationEvent
import monix.execution.atomic.AtomicBoolean
import org.scalatest.concurrent.Eventually.eventually
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

class SimulationEngineTest() extends AnyFunSuite with Matchers:
  test("A simulation must eventually complete") {
    val completedCalled = AtomicBoolean(false)
    val simulationEngine = SimulationEngine(DomainExample.environment)
    simulationEngine.changeSpeed(15)
    simulationEngine.resume()
    simulationEngine.addListener({
      case SimulationEvent.SimulationCompleted(_) =>
        completedCalled := true
      case _ => // ignore
    })
    eventually {
      completedCalled.get() mustBe true
    }
  }
  test(
    "A simulation that is paused and then resumed must eventually complete"
  ) {
    val completedCalled = AtomicBoolean(false)
    val simulationEngine = SimulationEngine(DomainExample.environment)
    simulationEngine.changeSpeed(15)
    simulationEngine.resume()
    simulationEngine.pause()
    simulationEngine.resume()
    simulationEngine.addListener({
      case SimulationEvent.SimulationCompleted(_) =>
        completedCalled := true
      case _ => // ignore
    })
    eventually {
      completedCalled.get() mustBe true
    }
  }
  test(
    "A terminated simulation must immediately complete"
  ) {
    val completedCalled = AtomicBoolean(false)
    val simulationEngine = SimulationEngine(DomainExample.environment)
    simulationEngine.changeSpeed(15)
    simulationEngine.resume()
    simulationEngine.addListener({
      case SimulationEvent.SimulationCompleted(_) =>
        completedCalled := true
      case _ => // ignore
    })
    simulationEngine.terminate()
    completedCalled.get() mustBe true
  }
