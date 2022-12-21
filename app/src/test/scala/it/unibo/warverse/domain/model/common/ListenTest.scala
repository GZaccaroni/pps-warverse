package it.unibo.warverse.domain.model.common

import it.unibo.warverse.domain.model.common.Listen.onReceiveEvent
import monix.execution.atomic.{AtomicAny, AtomicBoolean, AtomicInt}
import org.scalatest.Suites
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

object ListenTest:
  class SimpleListenableTest() extends AnyFunSuite with Matchers:
    test("Listeners must be informed one time when a new event is published") {
      val atomicString = AtomicAny(None: Option[String])
      val sample = TestListenable()
      val listener = (event: String) => atomicString := Some(event)
      val eventValue = "TestEvent"

      sample.addListener(listener)
      sample.emitEvent(eventValue)
      atomicString.get() mustBe Some(eventValue)
    }
    test(
      "Listeners must be informed every time a new event is published"
    ) {
      val atomicInt = AtomicInt(0)
      val sample = TestListenable()
      val listener = (_: String) => atomicInt.increment(1)

      sample.addListener(listener)
      sample.emitEvent("1")
      sample.emitEvent("2")
      atomicInt.get() mustBe 2
    }
    test(
      "After removal listeners must no more be informed"
    ) {
      val notified = AtomicBoolean(false)
      val sample = TestListenable()
      val listener = (_: String) => notified := true

      sample.addListener(listener)
      sample.removeListener(listener)
      sample.emitEvent("1")
      notified.get() mustBe false
    }

  class OnReceiveEventTest() extends AnyFunSuite with Matchers:
    test(
      "Listener registered with onReceiveEvent must be informed on new events"
    ) {
      val notified = AtomicBoolean(false)
      val sample = TestListenable()
      val listener = (_: String) => notified := true

      onReceiveEvent[String] from sample run listener
      sample.emitEvent("1")
      notified.get() mustBe true
    }
    test(
      "Listener registered with onReceiveEvent and then disposed must not be informed on new events"
    ) {
      val notified = AtomicBoolean(false)
      val sample = TestListenable()
      val listener = (_: String) => notified := true

      val disposable = onReceiveEvent[String] from sample run listener
      disposable.dispose()
      sample.emitEvent("1")
      notified.get() mustBe false
    }
  private class TestListenable() extends Listen.SimpleListenable[String]:
    override def emitEvent(event: String): Unit =
      super.emitEvent(event)

class ListenTest
    extends Suites(
      ListenTest.SimpleListenableTest(),
      ListenTest.OnReceiveEventTest()
    )
