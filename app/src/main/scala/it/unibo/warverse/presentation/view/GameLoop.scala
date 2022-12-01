package it.unibo.warverse.presentation.view

import it.unibo.warverse.presentation.inputs.GameMouseMotion
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Graphics
import it.unibo.warverse.domain.model.common.Geometry.Point2D
import it.unibo.warverse.domain.model.common.Geometry.Polygon2D
import it.unibo.warverse.domain.model.world.World.Country
import it.unibo.warverse.domain.model.common.Geometry
import java.awt.Polygon
import scala.language.postfixOps
import it.unibo.warverse.domain.model.fight.Army
import java.awt.BasicStroke
import it.unibo.warverse.presentation.common.UIConstants
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Toolkit
import it.unibo.warverse.domain.model.fight.Army.*
import it.unibo.warverse.presentation.controllers.*
import it.unibo.warverse.domain.model.Environment

class GameLoop:

  private var exit: Boolean = true
  private var paused: Boolean = false
  private var gameThread: Thread = _
  private val attackController = AttackController()
  private val movementController = MovementController()
  private val relationsController = RelationsController()
  private var nextLoop: Long = 0
  private val timeFrame = 1000
  private var _controller: GameStateController = _
  private var _environment: Environment = Environment.initial(List.empty)

  def environment: Environment = _environment
  def environment_=(environment: Environment): Unit =
    _controller.environment = environment
    _environment = environment

  def setController(controller: GameStateController): Unit =
    this._controller = controller

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

  def gameLoop(): Unit =
    waitForNextLoop()
    relationsController
      .updateRelations() // quali stati devono intervenire in guerra (forse in prolog)
    attackController.attackAndUpdate()
    /*setEnvironment(
      gameStateController
        .updateResources(environment)
    )*/
    checkAndUpdateEndedWars()
    movementController.moveUnitArmies()
    if continue() then gameLoop()

  private def waitForNextLoop(): Unit =
    try Thread.sleep(Math.max(0, nextLoop - System.currentTimeMillis()))
    catch case ex: InterruptedException => ()
    nextLoop = System.currentTimeMillis() + timeFrame

  private def continue(): Boolean =
    exit && !paused

  def checkAndUpdateEndedWars(): Unit =
    println("Check End War")
