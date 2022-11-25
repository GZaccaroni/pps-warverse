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
import it.unibo.warverse.controllers.*

class GameLoop extends Runnable:

  var exit = true
  var gameThread: Thread = _
  val attackController = AttackController()
  val movementController = MovementController()
  val relationsController = RelationsController()
  val gameMap = GameMap()

  def startGameLoop(): Unit =
    gameThread = new Thread(this)
    gameThread.start()

  def stopGameLoop(): Unit =
    exit = false
    gameThread.interrupt()

  override def run(): Unit =
    val timeFrame = 1000000000.0 / 120
    var lastFrame = System.nanoTime
    val now = System.nanoTime
    var frames = 0
    var lastCheck = System.currentTimeMillis
    while exit do
      if now - lastFrame >= timeFrame then
        gameloop()
        gameMap.repaint()
        lastFrame = now
        frames = frames + 1
      if System.currentTimeMillis() - lastCheck >= 1000 then
        lastCheck = System.currentTimeMillis()
        println("FPS " + frames)
        frames = 0

  //
  def gameloop(): Unit =
    waitForNextLoop()
    relationsController
      .updateRelations() // quali stati devono intervenire in guerra (forse in prolog)
    attackController.attackAndUpdate()
    updateResources() // i civili producono e i soldati consumano
    checkAndUpdateEndedWars()
    movementController.moveUnitArmys()
    updateVisualization()
    if continue() then gameloop()

  private def waitForNextLoop(): Unit = ???

  private def continue(): Boolean = ???

  def updateResources(): Unit = ???

  def checkAndUpdateEndedWars(): Unit = ???

  def updateVisualization(): Unit = ???
