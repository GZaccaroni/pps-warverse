package it.unibo.warverse.presentation.controllers

import it.unibo.warverse.presentation.view.*
import it.unibo.warverse.domain.model.world.Relations
import it.unibo.warverse.domain.model.world.Relations.*
import it.unibo.warverse.domain.model.world.World.Country
import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.presentation.common.UIConstants

trait GameStateController:
  def mainFrame: MainFrame
  def setPanel(): Unit
  def onStartClicked(): Unit
  def onPauseClicked(): Unit
  def onResumeClicked(): Unit
  def onStopClicked(): Unit
  var allCountries: Seq[Country]
  var interstateRelations: InterstateRelations
  def updateResources(environment: Environment): Environment
  var environment: Environment
  def show(x: Option[Country]): Country
  def isInWar(country: Country): Boolean

object GameStateController:
  def apply(mainFrame: MainFrame): GameStateController =
    GameStateControllerImpl(mainFrame)

  private class GameStateControllerImpl(override val mainFrame: MainFrame)
      extends GameStateController:

    private val gameLoop = GameLoop()

    private val gameMap = GameMap()

    private val hud = Hud()

    private val gamePanel = GamePanel()

    private var _interstateRelations: InterstateRelations = _

    def environment: Environment =
      gameMap.environment
    def environment_=(environment: Environment): Unit =
      gameMap.environment = environment

    override def isInWar(country: Country): Boolean =
      interstateRelations.countryEnemies(country.id).nonEmpty

    override def updateResources(environment: Environment): Environment =
      environment.copiedWith(
        countries = environment.countries
          .map(country =>
            country.updateResources(
              if isInWar(country) then
                country.citizens - country.armyUnits
                  .map(_.dailyConsume)
                  .sum
              else country.citizens
            )
          )
      )
    override def setPanel(): Unit =
      hud.setController(this)
      gameLoop.setController(this)
      gamePanel.addToPanel(gameMap, GuiEnum.WEST)
      gamePanel.addToPanel(hud, GuiEnum.EAST)
      mainFrame.setPanel(gamePanel)
      gameLoop.setController(this)

    override def onStartClicked(): Unit =
      gameLoop.startGameLoop()

    override def onPauseClicked(): Unit =
      gameLoop.pauseGameLoop()

    override def onResumeClicked(): Unit =
      gameLoop.resumeGameLoop()

    override def onStopClicked(): Unit =
      gameLoop.stopGameLoop()

    def allCountries_=(countries: Seq[Country]): Unit =
      this.gameLoop.setController(this)
      val newEnv = this.environment.copiedWith(countries = countries)
      gameMap.environment = newEnv
      gameLoop.environment = newEnv


    def allCountries: Seq[Country] =
      this.environment.countries

    def interstateRelations: InterstateRelations =
      this._interstateRelations

    def interstateRelations_=(
      interstateRelations: InterstateRelations
    ): Unit =
      this._interstateRelations = interstateRelations

    override def show(x: Option[Country]): Country = x match
      case Some(s) => s
      case None    => null
