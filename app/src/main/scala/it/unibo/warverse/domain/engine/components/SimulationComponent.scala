package it.unibo.warverse.domain.engine.components

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.common.Listen.Listenable
import it.unibo.warverse.domain.model.fight.SimulationEvent
import monix.eval.Task

/** Represents a simulation component that handle a particular phase of it and
  * can emit [[SimulationEvent]].
  */
trait SimulationComponent extends Listenable[SimulationEvent]:

  /** */
  def run(environment: Environment): Task[Environment]
