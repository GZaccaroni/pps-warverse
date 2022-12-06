package it.unibo.warverse.presentation.controllers

import it.unibo.warverse.domain.engine.SimulationEngine
import it.unibo.warverse.presentation.view.*
import it.unibo.warverse.domain.model.world.{GameStats, Relations}
import it.unibo.warverse.domain.model.world.Relations.*
import it.unibo.warverse.domain.model.world.World.Country
import it.unibo.warverse.domain.model.{Environment, SimulationConfig}
import it.unibo.warverse.presentation.common.UIConstants
import it.unibo.warverse.domain.model.common.Listen.*
import it.unibo.warverse.domain.model.fight.SimulationEvent
trait GameStateController:
  def mainFrame: MainFrame
  def setPanel(): Unit
  def onStartClicked(simulationConfig: SimulationConfig): Unit
  def onPauseClicked(): Unit
  def onResumeClicked(): Unit
  def onStopClicked(): Unit

object GameStateController:
  def apply(mainFrame: MainFrame): GameStateController =
    GameStateControllerImpl(mainFrame)

  private class GameStateControllerImpl(override val mainFrame: MainFrame)
      extends GameStateController:

    private var simulationEngine: Option[SimulationEngine] = None

    private val gameMap = GameMap()

    private val hud = Hud()

    private val gamePanel = GamePanel()

    private val gameStats = GameStats()

    override def setPanel(): Unit =
      hud.setController(this)
      gamePanel.addToPanel(gameMap, GuiEnum.WEST)
      gamePanel.addToPanel(hud, GuiEnum.EAST)
      mainFrame.setPanel(gamePanel)

    override def onStartClicked(simulationConfig: SimulationConfig): Unit =
      if simulationEngine.isEmpty then
        simulationEngine = Some(SimulationEngine(simulationConfig))
        for simulationEngine <- simulationEngine do
          simulationEngine.start()
          onReceiveEvent[SimulationEvent] from simulationEngine run onEvent
    override def onPauseClicked(): Unit =
      print("Pause clicked")
      simulationEngine foreach (_.pause())

    override def onResumeClicked(): Unit =
      simulationEngine foreach (_.resume())

    override def onStopClicked(): Unit =
      simulationEngine foreach (_.terminate())
      simulationEngine = None

    private def onEvent(event: SimulationEvent): Unit =
      event match
        case SimulationEvent.SimulationStarted(environment) =>
          gameMap.environment = Some(environment)
        case SimulationEvent.IterationCompleted(environment) =>
          gameMap.environment = Some(environment)
        case SimulationEvent.SimulationCompleted =>
          mainFrame.setPanel(EndPanel())
        case SimulationEvent.CountryWonWar(winnerId, loserId, day) =>
          this.gameStats.updateEventList(
            winnerId,
            loserId,
            day
          )
