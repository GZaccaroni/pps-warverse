package it.unibo.warverse.domain.engine

import it.unibo.warverse.domain.engine.components.SimulationComponent
import it.unibo.warverse.domain.model.{Environment, SimulationConfig}
import it.unibo.warverse.domain.model.common.Geometry.{Point2D, Polygon2D}
import it.unibo.warverse.domain.model.common.Listen.*
import it.unibo.warverse.domain.model.common.{Disposable, Geometry}
import it.unibo.warverse.domain.model.fight.Army.*
import it.unibo.warverse.domain.model.fight.{Army, SimulationEvent}
import it.unibo.warverse.domain.model.fight.SimulationEvent.*
import it.unibo.warverse.domain.model.world.GameStats
import it.unibo.warverse.domain.model.world.Relations.InterstateRelations
import it.unibo.warverse.domain.model.world.World.Country
import it.unibo.warverse.domain.engine.components.*
import monix.eval.Task
import monix.execution.Cancelable

import concurrent.duration.DurationInt
import monix.execution.Scheduler.Implicits.global
import monix.execution.Scheduler.global as scheduler

import scala.annotation.tailrec
import scala.concurrent.Await

trait SimulationEngine extends Listenable[SimulationEvent]:
  def simulationConfig: SimulationConfig
  def currentEnvironment: Environment
  def start(): Unit
  def resume(): Unit
  def pause(): Unit
  def terminate(): Unit

object SimulationEngine:
  def apply(simulationConfig: SimulationConfig): SimulationEngine =
    SimulationEngineImpl(simulationConfig)

  private class SimulationEngineImpl(val simulationConfig: SimulationConfig)
      extends SimpleListenable[SimulationEvent]
      with SimulationEngine:

    override def currentEnvironment: Environment = environment
    private val simulationComponents: Seq[SimulationComponent] = List(
      AttackSimulationComponent(),
      MovementSimulationComponent(),
      RelationsSimulationComponent(),
      ResourcesSimulationComponent(),
      WarSimulationComponent()
    )
    private var taskCancelable: Option[Cancelable] = None
    private var environment = Environment(
      simulationConfig.countries,
      simulationConfig.interstateRelations
    )
    private val disposables: Seq[Disposable] =
      simulationComponents.map { component =>
        onReceiveEvent[SimulationEvent] from component run handleEvent
      }
    override def start(): Unit =
      runLoop()

    override def resume(): Unit =
      runLoop()

    override def pause(): Unit =
      taskCancelable foreach (_.cancel())

    override def terminate(): Unit =
      taskCancelable foreach (_.cancel())
      emitEvent(SimulationCompleted)
      disposables foreach (_.dispose)

    private def runLoop(): Unit =
      if taskCancelable.isEmpty then
        taskCancelable = Some(
          scheduler.scheduleAtFixedRate(0.seconds, 1.seconds) {
            simulationComponents
              .foldLeft(Task(environment)) { (task, simulationComponent) =>
                task.flatMap(simulationComponent.run)
              }
            this.environment = environment
            emitEvent(IterationCompleted(environment))

            checkEnd()
          }
        )

    private def handleEvent(event: SimulationEvent): Unit =
      emitEvent(event)

    private def checkEnd(): Unit =
      if noWars(environment)
      then terminate()

    private def noWars(
      environment: Environment
    ): Boolean =
      environment.countries.forall(country =>
        environment.interstateRelations.countryEnemies(country.id).isEmpty
      )
