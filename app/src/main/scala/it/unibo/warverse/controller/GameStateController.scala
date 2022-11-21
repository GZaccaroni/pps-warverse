package it.unibo.warverse.controller

import it.unibo.warverse.ui.view.GameMap
import it.unibo.warverse.ui.view.Hud
import it.unibo.warverse.ui.view.GamePanel
import java.awt.BorderLayout
import it.unibo.warverse.ui.view.MainFrame

class GameStateController:
  var mainFrame: MainFrame = null

  val gameMap = new GameMap()

  val hud = new Hud()

  val gamePanel = new GamePanel()

  def setMain(mainFrame: MainFrame): Unit =
    this.mainFrame = mainFrame
    setPanel()

  def setPanel(): Unit =
    gamePanel.addToPanel(gameMap, BorderLayout.WEST)
    hud.setController(this)
    gamePanel.addToPanel(hud, BorderLayout.EAST)
    mainFrame.setPanel(gamePanel)

  def startClicked(): Unit =
    gameMap.startGameLoop()

  def stopClicked(): Unit =
    gameMap.stopGameLoop()
