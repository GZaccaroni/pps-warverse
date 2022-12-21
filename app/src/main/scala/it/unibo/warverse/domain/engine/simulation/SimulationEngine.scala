package it.unibo.warverse.domain.engine.simulation

import it.unibo.warverse.domain.engine.simulation.components.*
import it.unibo.warverse.domain.model.common.Geometry.Point2D
import it.unibo.warverse.domain.model.common.Listen.*
import it.unibo.warverse.domain.model.common.{Disposable, Geometry}
import it.unibo.warverse.domain.model.common.dispose
import it.unibo.warverse.domain.model.fight.Army.*
import it.unibo.warverse.domain.model.fight.SimulationEvent.*
import it.unibo.warverse.domain.model.fight.{Army, SimulationEvent}
import it.unibo.warverse.domain.model.world.Relations.InterCountryRelations
import it.unibo.warverse.domain.model.world.SimulationStats
import it.unibo.warverse.domain.model.world.World.Country
import it.unibo.warverse.domain.model.Environment
import monix.eval.Task
import monix.execution.Cancelable
import monix.execution.Scheduler.Implicits.global
import monix.execution.Scheduler.global as scheduler
import monix.execution.atomic.{Atomic, AtomicAny, AtomicBoolean}
import monix.reactive.Observable
import monix.reactive.ObservableLike.fromTask

import scala.annotation.tailrec
import scala.concurrent.Await
import scala.concurrent.duration.{DurationDouble, DurationInt, FiniteDuration}

/** Handles the simulation of the war, it can emit [[SimulationEvent]].
  */
trait SimulationEngine extends Listenable[SimulationEvent]:
  /** The current environment of the simulation
    *
    * @return
    *   The current environment
    */
  def currentEnvironment: Environment

  /** Resumes the simulation, if simulation is terminated or already running the
    * command is ignored
    */
  def resume(): Unit

  /** Pauses the simulation */
  def pause(): Unit

  /** Terminates the simulation */
  def terminate(): Unit

  /** Changes the speed of the simulation
    * @param newSpeed
    *   the new speed of the simulation
    * @throws IllegalArgumentException
    *   if speed is not greater than zero
    */
  def changeSpeed(newSpeed: Int): Unit

object SimulationEngine:
  /** Creates an instance of [[SimulationEngine]]
    * @param environment
    *   The initial config of the simulation
    * @return
    *   A SimulationEngine
    */
  def apply(environment: Environment): SimulationEngine =
    SimulationEngineImpl(environment)

  private class SimulationEngineImpl(initialEnvironment: Environment)
      extends SimpleListenable[SimulationEvent]
      with SimulationEngine:

    override def currentEnvironment: Environment = environment.get()
    private val simulationComponents: Seq[SimulationComponent] = List(
      AttackSimulationComponent(),
      MovementSimulationComponent(),
      RelationsSimulationComponent(),
      ResourcesSimulationComponent(),
      WarSimulationComponent()
    )
    private val simulationDayDuration: FiniteDuration = 1.5.seconds
    private var speed = 1
    private var loopCancelable: Option[Cancelable] = None
    private val isRunning: Atomic[Boolean] = AtomicBoolean(false)
    private val isTerminated: Atomic[Boolean] = AtomicBoolean(false)
    private val environment: Atomic[Environment] = AtomicAny(
      initialEnvironment
    )

    override def resume(): Unit =
      runLoop()

    override def pause(): Unit =
      if isRunning.get() then
        loopCancelable foreach (_.cancel())
        loopCancelable = None

    override def changeSpeed(newSpeed: Int): Unit =
      if speed <= 0 then
        throw IllegalArgumentException("Speed must be greater than 0")
      if speed != newSpeed then
        if isRunning.get() then
          pause()
          speed = newSpeed
          resume()
        else speed = newSpeed

    override def terminate(): Unit =
      if !isTerminated.getAndSet(true) then
        loopCancelable foreach (_.cancel())
        loopCancelable = None
        emitEvent(SimulationCompleted(this.environment.get()))
        isTerminated := true

    private def runLoop(): Unit =
      if !isTerminated.get() && !isRunning.getAndSet(true) then
        val disposables: Seq[Disposable] = registerComponentListeners()
        val task = Observable
          .intervalAtFixedRate(simulationDayDuration / speed)
          .scanEval(Task(currentEnvironment)) { (previous, _) =>
            simulationComponents
              .foldLeft(Task(previous)) { (task, simulationComponent) =>
                task.flatMap(simulationComponent.run)
              }
          }
          .takeWhileInclusive(_.interCountryRelations.hasOngoingWars)
          .foreachL(newEnvironment =>
            this.environment := newEnvironment
            emitEvent(IterationCompleted(newEnvironment))
          )
          .guarantee(Task {
            loopCancelable = None
            isRunning := false
            disposables.dispose()
          })
          .doOnFinish(_ =>
            Task {
              isTerminated := true
              emitEvent(SimulationCompleted(currentEnvironment))
            }
          )
        loopCancelable = Some(task.runAsync(_ => ()))
    private def handleEvent(event: SimulationEvent): Unit =
      emitEvent(event)

    private def registerComponentListeners(): Seq[Disposable] =
      simulationComponents.map { component =>
        onReceiveEvent[SimulationEvent] from component run handleEvent
      }
