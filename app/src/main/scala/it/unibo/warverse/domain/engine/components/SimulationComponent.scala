package it.unibo.warverse.domain.engine.components

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.common.Listen.Listenable
import it.unibo.warverse.domain.model.fight.SimulationEvent

trait SimulationComponent extends Listenable[SimulationEvent]:
  def run(environment: Environment): Environment