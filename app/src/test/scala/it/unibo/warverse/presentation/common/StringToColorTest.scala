package it.unibo.warverse.presentation.common

import it.unibo.warverse.domain.model.common.DomainExample
import it.unibo.warverse.domain.model.fight.SimulationEvent
import monix.execution.atomic.AtomicBoolean
import org.scalatest.concurrent.Eventually.eventually
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

class StringToColorTest extends AnyFunSuite with Matchers:
  test("A color must be provided for an empty string ") {
    noException mustBe thrownBy("".toColor)
  }
  test(
    "A color must be provided for an arbitrary long string"
  ) {
    noException mustBe thrownBy("cane".toColor)
  }
