package it.unibo.warverse.presentation.controllers

import it.unibo.warverse.presentation.view.*
import it.unibo.warverse.domain.model.world.Relations

import java.awt.BorderLayout
import it.unibo.warverse.domain.model.world.Relations.*
import it.unibo.warverse.domain.model.world.World.Country

trait GameStateController:
  def mainFrame: MainFrame
  def setPanel(): Unit
  def startClicked(): Unit
  def stopClicked(): Unit
  def setAllCountries(countries: List[Country]): Unit
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

    private val gameLoop = GameLoop()

    private val gameMap = GameMap()

    private val hud = Hud()

    private val gamePanel = GamePanel()

    var interstateRelation: InterstateRelations = _
    var countries: List[Country] = List()

    override def setPanel(): Unit =
      gamePanel.addToPanel(gameMap, BorderLayout.WEST)
      hud.setController(this)
      gamePanel.addToPanel(hud, BorderLayout.EAST)
      mainFrame.setPanel(gamePanel)

    override def startClicked(): Unit =
      gameLoop.startGameLoop()

    override def stopClicked(): Unit =
      gameLoop.stopGameLoop()

    override def setAllCountries(countries: List[Country]): Unit =
      this.countries = countries

    override def getRelationship: InterstateRelations =
      this.interstateRelation

    override def setInterstateRelations(
      interstateRelations: InterstateRelations
    ): Unit =
      this.interstateRelation = interstateRelations

    override def show(x: Option[Country]): Country = x match
      case Some(s) => s
      case None    => null
