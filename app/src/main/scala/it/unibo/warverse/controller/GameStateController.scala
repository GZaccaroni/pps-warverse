package it.unibo.warverse.controller

import it.unibo.warverse.ui.view.*
import it.unibo.warverse.model.world.Relations

import java.awt.BorderLayout
import it.unibo.warverse.model.world.Relations.InterstateRelations
import it.unibo.warverse.model.world.World.Country
import it.unibo.warverse.model.world.Relations.Relation

import scala.language.postfixOps

class GameStateController:
  var mainFrame: MainFrame = _

  val gameLoop = GameLoop()

  val gameMap = GameMap()

  val hud = Hud()

  val gamePanel = GamePanel()

  var interstateRelation: InterstateRelations = _
  var countries: List[Country] = List()

  def setMain(mainFrame: MainFrame): Unit =
    this.mainFrame = mainFrame
    setPanel()

  def setPanel(): Unit =
    gamePanel.addToPanel(gameMap, BorderLayout.WEST)
    hud.setController(this)
    gamePanel.addToPanel(hud, BorderLayout.EAST)
    mainFrame.setPanel(gamePanel)

  def startClicked(): Unit =
    gameLoop.startGameLoop()

  def stopClicked(): Unit =
    gameLoop.stopGameLoop()

  def setAllCountries(countries: List[Country]): Unit =
    this.countries = countries

  def getRelationship(): InterstateRelationsImpl =
    this.interstateRelation

  def setRelationship(
    allianceList: Map[String, List[String]],
    enemyList: Map[String, List[String]]
  ): Unit =
    interstateRelation = InterstateRelations(
      cycleMapRelations(allianceList, false) ++ cycleMapRelations(
        enemyList,
        true
      )
    )

  def cycleMapRelations(
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

  def show(x: Option[Country]): Country = x match
    case Some(s) => s
    case None    => null
