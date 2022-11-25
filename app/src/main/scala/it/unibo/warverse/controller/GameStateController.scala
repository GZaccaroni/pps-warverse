package it.unibo.warverse.controller

import it.unibo.warverse.ui.view.*
import it.unibo.warverse.model.world.Relations

import java.awt.BorderLayout
import it.unibo.warverse.model.world.Relations.*
import it.unibo.warverse.model.world.World.Country

import scala.language.postfixOps

trait GameStateController:
  def mainFrame: MainFrame
  def setPanel(): Unit
  def startClicked(): Unit
  def stopClicked(): Unit
  def setAllCountries(countries: List[Country]): Unit
  def getRelationship: InterstateRelations
  def setRelationship(
    allianceList: Map[String, List[String]],
    enemyList: Map[String, List[String]]
  ): Unit
  def cycleMapRelations(
    map: Map[String, List[String]],
    isEnemy: Boolean
  ): List[Relation]
  def show(x: Option[Country]): Country

object GameStateController:
  def apply(mainFrame: MainFrame): GameStateController =
    GameStateControllerImpl(mainFrame)

  class GameStateControllerImpl(override val mainFrame: MainFrame)
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

    override def setRelationship(
      allianceList: Map[String, List[String]],
      enemyList: Map[String, List[String]]
    ): Unit =
      interstateRelation = InterstateRelations(
        cycleMapRelations(allianceList, false) ++ cycleMapRelations(
          enemyList,
          true
        )
      )

    override def cycleMapRelations(
      map: Map[String, List[String]],
      isEnemy: Boolean
    ): List[Relation] =
      var relations: List[Relation] = List()
      map.map((k, value) =>
        value.foreach(countryName =>
          val currentCountry: Option[Country] =
            this.countries.find(countryMap => countryMap.name == k)
          val selected: Option[Country] =
            this.countries.find(countryMap => countryMap.name == countryName)

          relations = relations ++ List(
            (
              (show(currentCountry), show(selected)),
              if isEnemy then Relations.RelationStatus.WAR
              else Relations.RelationStatus.ALLIANCE
            )
          )
        )
      )
      relations

    override def show(x: Option[Country]): Country = x match
      case Some(s) => s
      case None    => null
