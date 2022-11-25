package it.unibo.warverse.ui.view

import it.unibo.warverse.ui.inputs.GameMouseMotion
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Graphics
import it.unibo.warverse.model.common.Geometry.Point2D
import it.unibo.warverse.model.common.Geometry.Polygon2D
import it.unibo.warverse.model.world.World.Country
import it.unibo.warverse.model.world.World.Citizen
import it.unibo.warverse.model.common.Geometry
import java.awt.Polygon
import scala.language.postfixOps
import it.unibo.warverse.model.fight.Army
import java.awt.BasicStroke
import it.unibo.warverse.ui.common.UIConstants
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Toolkit
import it.unibo.warverse.model.fight.Army.*
import it.unibo.warverse.controller.*

class GameLoop:

  var exit: Boolean = true
  var gameThread: Thread = _
  val attackController = AttackController()
  val movementController = MovementController()
  val relationsController = RelationsController()
  val gameMap = GameMap()
  private var nextLoop: Long = 0
  private val timeFrame = 1000

  def startGameLoop(): Unit =
    gameThread = Thread(() => gameLoop())
    gameThread.start()

  def stopGameLoop(): Unit =
    exit = false
    gameThread.interrupt()

  def gameLoop(): Unit =
    waitForNextLoop()
    relationsController
      .updateRelations() // quali stati devono intervenire in guerra (forse in prolog)
    attackController.attackAndUpdate()
    updateResources() // i civili producono e i soldati consumano
    checkAndUpdateEndedWars()
    movementController.moveUnitArmies()
    updateVisualization()
    if continue() then gameLoop()

  private def waitForNextLoop(): Unit =
    try Thread.sleep(Math.max(0, nextLoop - System.currentTimeMillis()))
    catch case ex: InterruptedException => ()
    nextLoop = System.currentTimeMillis() + timeFrame

  private def continue(): Boolean =
    exit

  def updateResources(): Unit =
    println("Update Res")

  def checkAndUpdateEndedWars(): Unit =
    println("Check End War")

  def updateVisualization(): Unit =
    this.gameMap.repaint()
