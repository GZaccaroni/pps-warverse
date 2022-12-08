package it.unibo.warverse.domain.engine

import it.unibo.warverse.domain.engine.components.SimulationComponent
import it.unibo.warverse.domain.model.{Environment, SimulationConfig}
import it.unibo.warverse.domain.model.common.Geometry.{Point2D, Polygon2D}
import it.unibo.warverse.domain.model.common.Listen.*
import it.unibo.warverse.domain.model.common.{Disposable, Geometry}
import it.unibo.warverse.domain.model.fight.Army.*
import it.unibo.warverse.domain.model.fight.{Army, SimulationEvent}
import it.unibo.warverse.domain.model.fight.SimulationEvent.*
import it.unibo.warverse.domain.model.world.SimulationStats
import it.unibo.warverse.domain.model.world.Relations.InterCountryRelations
import it.unibo.warverse.domain.model.world.World.Country
import it.unibo.warverse.domain.engine.components.*
import monix.eval.Task
import monix.execution.Cancelable

import concurrent.duration.{DurationInt, DurationDouble, FiniteDuration}
import monix.execution.Scheduler.Implicits.global
import monix.execution.Scheduler.global as scheduler
import monix.reactive.Observable
import monix.reactive.ObservableLike.fromTask

import scala.annotation.tailrec
import scala.concurrent.Await

/** Handles the simulation of the war, it can emit {@link SimulationEvent}.
  */
trait SimulationEngine extends Listenable[SimulationEvent]:
  /** The initial simulation config
    * @return
    *   The initial simulation config
    */
  def simulationConfig: SimulationConfig

  /** The current environment of the simulation
    *
    * @return
    *   The current environment
    */
  def currentEnvironment: Environment

  /** Starts the simulation
    */
  def start(): Unit

  /** Resumes the simulation
    */
  def resume(): Unit

  /** Pauses the simulation */
  def pause(): Unit

  /** Terminates the simulation */
  def terminate(): Unit
  def changeSpeed(newSpeed: Int): Unit

object SimulationEngine:
  /** Creates an instance of {@link SimulationEngine}
    * @param simulationConfig
    *   The initial config of the simulation
    * @return
    *   A SimulationEngine
    */
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
    private val simulationDayDuration: FiniteDuration = 1.5.seconds
    private var speed = 1
    private var taskCancelable: Option[Cancelable] = None
    private var environment = Environment(
      simulationConfig.countries,
      simulationConfig.interCountryRelations
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
      taskCancelable = None
    private def isRunning: Boolean = taskCancelable.nonEmpty

    override def changeSpeed(newSpeed: Int): Unit =
      if speed != newSpeed then
        if isRunning then
          pause()
          speed = newSpeed
          resume()
        else speed = newSpeed

    override def terminate(): Unit =
      taskCancelable foreach (_.cancel())
      taskCancelable = None
      emitEvent(SimulationCompleted(this.environment))
      disposables foreach (_.dispose)

    private def runLoop(): Unit =
      if taskCancelable.isEmpty then
        val task = Observable
          .intervalAtFixedRate(simulationDayDuration / speed)
          .scanEval(Task(environment)) { (previous, _) =>
            simulationComponents
              .foldLeft(Task(previous)) { (task, simulationComponent) =>
                task.flatMap(simulationComponent.run)
              }
          }
          .foreachL(newEnvironment =>
            this.environment = newEnvironment
            emitEvent(IterationCompleted(newEnvironment))
            if !warsExists(newEnvironment) then terminate()
          )
        taskCancelable = Some(task.runAsync(_ => ()))
    private def handleEvent(event: SimulationEvent): Unit =
      emitEvent(event)

    private def warsExists(
      environment: Environment
    ): Boolean =
      if environment.countries.size > 0 then
        environment.countries.forall(country =>
          environment.interCountryRelations.countryEnemies(country.id).nonEmpty
        )
      else false
