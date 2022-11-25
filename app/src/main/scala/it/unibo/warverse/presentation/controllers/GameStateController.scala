package it.unibo.warverse.controllers

import it.unibo.warverse.presentation.view.*
import it.unibo.warverse.domain.model.world.Relations

import java.awt.BorderLayout
import it.unibo.warverse.domain.model.world.Relations.*
import it.unibo.warverse.domain.model.world.World.Country

import scala.language.postfixOps

class GameStateController:
  var mainFrame: MainFrame = _

  val gameLoop = GameLoop()

  val gameMap = GameMap()

  val hud = Hud()

  val gamePanel = GamePanel()

  var interstateRelations: InterstateRelations = _
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

  def getRelationship(): InterstateRelations =
    this.interstateRelations

  def setInterstateRelations(
    interstateRelations: InterstateRelations
  ): Unit =
    this.interstateRelations = interstateRelations

  def cycleMapRelations(
    map: Map[String, List[String]],
    isEnemy: Boolean
  ): List[InterstateRelation] =
    var relations: List[InterstateRelation] = List()
    map.map((k, value) =>
      value.foreach(countryName =>
        val currentCountry: Option[Country] =
          this.countries.find(countryMap => countryMap.name == k)
        val selected: Option[Country] =
          this.countries.find(countryMap => countryMap.name == countryName)

        relations = relations ++ List(
          (
            (show(currentCountry).id, show(selected).id),
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
