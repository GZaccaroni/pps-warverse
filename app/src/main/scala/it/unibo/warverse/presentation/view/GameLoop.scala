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
import it.unibo.warverse.domain.model.world.Relations.InterstateRelations

class GameLoop:

  private var exit: Boolean = true
  private var paused: Boolean = false
  private var gameThread: Thread = _
  private val attackController = AttackController()
  private val movementController = MovementController()
  private val relationsController = RelationsController()
  private var gameStateController: GameStateController = _
  private val gameStatsController: GameStatsController = GameStatsController()
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
    this._controller.mainFrame.setPanel(EndPanel())

  def gameLoop(): Unit =
    waitForNextLoop()
    this.gameStateController.setInterstateRelations(
      relationsController
        .updateRelations(
          this.gameStateController.getRelationship,
          this.environment.countries
        )
    )
    attackController.attackAndUpdate()
    /*setEnvironment(
      gameStateController
        .updateResources(environment)
    )*/
    checkAndUpdateEndedWars()
    movementController.moveUnitArmies()
    checkEnd()
    if continue() then gameLoop()

  private def waitForNextLoop(): Unit =
    try Thread.sleep(Math.max(0, nextLoop - System.currentTimeMillis()))
    catch case ex: InterruptedException => ()
    nextLoop = System.currentTimeMillis() + timeFrame

  def checkEnd(): Unit =
    val currentRelation = this.gameStateController.getRelationship
    val currentCountries = this.environment.countries
    if this.relationsController.noWars(
        currentRelation,
        currentCountries
      )
    then stopGameLoop()

  def checkAndUpdateEndedWars(): Unit =
    val currentRelation = this.gameStateController.getRelationship
    val currentCountries = this.environment.countries
    this.relationsController
      .getWars(currentRelation, currentCountries)
      .foreach(countryInWar =>
        if countryInWar.armyUnits.size <= 0 || countryInWar.citizens <= 0 || countryInWar.resources <= 0
        then
          assignLostResources(countryInWar, currentCountries, currentRelation)
      )
    this.setEnvironment(this.environment.nextDay())

  private def assignLostResources(
    countryDefeated: Country,
    currentCountries: List[Country],
    currentRelation: InterstateRelations
  ): Unit =
    val winnersId = currentRelation.getEnemies(countryDefeated.id)
    val lostResources = countryDefeated.resources
    val lostCitizen = countryDefeated.citizens
    val lostArmy = countryDefeated.armyUnits
    val unitIndex = lostArmy.size / winnersId.size
    val citizenIndex = lostCitizen / winnersId.size
    val resourcesIndex = lostResources / winnersId.size
    this.gameStateController.setInterstateRelations(
      this.relationsController
        .removeLostStateRelation(countryDefeated, currentRelation)
    )
    this.setEnvironment(
      this.environment
        .setCountries(currentCountries.filterNot(_ == countryDefeated))
    )
    winnersId.toSeq
      .foreach(winnerId =>
        val c = winnersId.toSeq.indexOf(winnerId)
        val currentCountries = this.environment.getCountries
        val idToCountry = currentCountries
          .find(country => country.id == winnerId)
          .get
        val index = currentCountries.indexOf(
          idToCountry
        )
        val winnerCountry: Country =
          idToCountry
            .updateResources(
              if lostResources - resourcesIndex * (c + 1) < resourcesIndex then
                lostResources - resourcesIndex * c
              else resourcesIndex
            )
            .updateArmy(
              lostArmy
                .drop(unitIndex * c)
                .take(
                  if winnersId.size - 1 == c then lostArmy.size
                  else unitIndex
                )
            )
            .updateCitizen(
              if lostCitizen - citizenIndex * (c + 1) < citizenIndex then
                lostCitizen - citizenIndex * c
              else citizenIndex
            )
        this.gameStatsController.updateStatsEvents(
          winnerId,
          countryDefeated,
          this.environment.day
        )
        this.setEnvironment(
          this.environment
            .updateCountries(
              currentCountries.updated(
                index,
                winnerCountry
              )
            )
        )
      )

  private def continue(): Boolean =
    exit && !paused
