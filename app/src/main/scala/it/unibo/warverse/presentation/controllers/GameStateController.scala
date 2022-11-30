package it.unibo.warverse.presentation.controllers

import it.unibo.warverse.presentation.view.*
import it.unibo.warverse.domain.model.world.Relations

import java.awt.BorderLayout
import it.unibo.warverse.domain.model.world.Relations.*
import it.unibo.warverse.domain.model.world.World.Country
import it.unibo.warverse.domain.model.Environment

trait GameStateController:
  def mainFrame: MainFrame
  def setPanel(): Unit
  def startClicked(): Unit
  def pauseClicked(): Unit
  def resumeClicked(): Unit
  def stopClicked(): Unit
  def setAllCountries(countries: List[Country]): Unit
  def getAllCountries(): List[Country]
  def getRelationship: InterstateRelations
  def setInterstateRelations(
    interstateRelations: InterstateRelations
  ): Unit
  def show(x: Option[Country]): Country

object GameStateController:
  def apply(mainFrame: MainFrame): GameStateController =
    GameStateControllerImpl(mainFrame)

  private class GameStateControllerImpl(override val mainFrame: MainFrame)
      extends GameStateController:

    private val environment: Environment = Environment(List(), 0)

    private val gameLoop = GameLoop()

    private val gameMap = GameMap()

    private val hud = Hud()

    private val gamePanel = GamePanel()

    var interstateRelation: InterstateRelations = _

    override def setPanel(): Unit =
      hud.setController(this)
      gamePanel.addToPanel(gameMap, BorderLayout.WEST)
      gamePanel.addToPanel(hud, BorderLayout.EAST)
      mainFrame.setPanel(gamePanel)

    override def startClicked(): Unit =
      gameLoop.startGameLoop()

    override def pauseClicked(): Unit =
      gameLoop.pauseGameLoop()

    override def resumeClicked(): Unit =
      gameLoop.resumeGameLoop()

    override def stopClicked(): Unit =
      gameLoop.stopGameLoop()

    override def setAllCountries(countries: List[Country]): Unit =
      val newEnv = this.environment.updateCountries(countries)
      gameMap.setEnvironment(newEnv)
      gameLoop.setEnvironment(newEnv)

    override def getAllCountries(): List[Country] =
      this.environment.getCountries()

    override def getRelationship: InterstateRelations =
      this.interstateRelation

    override def setInterstateRelations(
      interstateRelations: InterstateRelations
    ): Unit =
      this.interstateRelation = interstateRelations

    override def show(x: Option[Country]): Country = x match
      case Some(s) => s
      case None    => null
