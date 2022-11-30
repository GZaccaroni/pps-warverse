package it.unibo.warverse.presentation.view

import it.unibo.warverse.presentation.inputs.GameMouseMotion
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Graphics
import it.unibo.warverse.domain.model.common.Geometry.Point2D
import it.unibo.warverse.domain.model.common.Geometry.Polygon2D
import it.unibo.warverse.domain.model.world.World.Country
import it.unibo.warverse.domain.model.world.World.Citizen
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
  private var gameStateController: GameStateController = _
  private var nextLoop: Long = 0
  private val timeFrame = 1000
  var environment: Environment = Environment()

  def setEnvironment(environment: Environment): Unit =
    this.gameStateController.setMapEnv(environment)
    this.environment = environment

  def setController(controller: GameStateController): Unit =
    this.gameStateController = controller

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
    this.gameStateController.mainFrame.setPanel(EndPanel())

  def gameLoop(): Unit =
    waitForNextLoop()
    relationsController
      .updateRelations(this.gameStateController.getRelationship, this.environment.countries) // quali stati devono intervenire in guerra (forse in prolog)
    attackController.attackAndUpdate()
    setEnvironment(
      gameStateController
        .updateResources(environment)
    )
    checkAndUpdateEndedWars()
    movementController.moveUnitArmies()
    if continue() then gameLoop()

  private def waitForNextLoop(): Unit =
    try Thread.sleep(Math.max(0, nextLoop - System.currentTimeMillis()))
    catch case ex: InterruptedException => ()
    nextLoop = System.currentTimeMillis() + timeFrame

  def checkAndUpdateEndedWars(): Unit =
    if this.relationsController.noWars(this.gameStateController.getRelationship, this.environment.countries) then stopGameLoop()

  private def continue(): Boolean =
    exit && !paused
