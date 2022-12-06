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

import scala.annotation.tailrec

class SimulationEngine(val simulationConfig: SimulationConfig)
    extends Listenable[SimulationEvent]:

  private var exit: Boolean = true
  private var paused: Boolean = false
  private var gameThread: Thread = _
  private val simulationComponents: Seq[SimulationComponent] = List(
    AttackSimulationComponent(),
    MovementSimulationComponent(),
    RelationsSimulationComponent(),
    ResourcesSimulationComponent(),
    WarSimulationComponent()
  )
  private var nextLoopTime: Long = 0
  private val timeFrame = 1000
  private var environment = Environment(
    simulationConfig.countries,
    simulationConfig.interstateRelations
  )

  private val cancellables: Seq[Disposable] =
    simulationComponents.map { component =>
      onReceiveEvent[SimulationEvent] from component run handleEvent
    }
  def startGameLoop(): Unit =
    gameThread = Thread(() => gameLoop())
    gameThread.start()

  def resumeGameLoop(): Unit =
    paused = false
    startGameLoop()

  def pauseGameLoop(): Unit =
    paused = true

  def stopGameLoop(): Unit =
    exit = false
    emitEvent(SimulationCompletedEvent)
    cancellables foreach (_.dispose)

  @tailrec
  private def gameLoop(): Unit =
    waitForNextLoop()
    environment = simulationComponents.foldLeft(environment) {
      (environment, simulationComponent) =>
        environment after simulationComponent.run
    }
    emitEvent(IterationCompleted(environment))

    checkEnd()
    if continue() then gameLoop()

  private def handleEvent(event: SimulationEvent): Unit =
    emitEvent(event)

  private def waitForNextLoop(): Unit =
    try Thread.sleep(Math.max(0, nextLoopTime - System.currentTimeMillis()))
    catch case ex: InterruptedException => ()
    nextLoopTime = System.currentTimeMillis() + timeFrame

  private def checkEnd(): Unit =
    if noWars(environment)
    then stopGameLoop()

  private def noWars(
    environment: Environment
  ): Boolean =
    environment.countries.forall(country =>
      environment.interstateRelations.countryEnemies(country.id).isEmpty
    )

  private def continue(): Boolean =
    exit && !paused

  extension (environment: Environment)
    private def after(
      closure: Environment => Environment
    ): Environment =
      closure(environment)
