package it.unibo.warverse.presentation.controllers

import it.unibo.warverse.domain.engine.simulation.SimulationEngine
import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.presentation.view.*
import it.unibo.warverse.domain.model.world.SimulationStats
import it.unibo.warverse.domain.model.common.Listen.*
import it.unibo.warverse.domain.model.fight.SimulationEvent

import java.awt.EventQueue
trait GameStateController:
  def mainFrame: MainFrame
  var simulationEnvironment: Option[Environment]
  def setPanel(): Unit
  def onStartClicked(): Unit
  def onPauseClicked(): Unit
  def onResumeClicked(): Unit
  def onStopClicked(): Unit
  def changeSpeed(newSpeed: Int): Unit

object GameStateController:
  def apply(mainFrame: MainFrame): GameStateController =
    GameStateControllerImpl(mainFrame)

  private class GameStateControllerImpl(override val mainFrame: MainFrame)
      extends GameStateController:

    private var simulationEngine: Option[SimulationEngine] = None

    private val gameMap = GameMap()

    private val hud = Hud(this)

    private val gamePanel = GamePanel()

    private val gameStats = SimulationStats()

    def simulationEnvironment: Option[Environment] =
      simulationEngine.map(_.currentEnvironment)

    def simulationEnvironment_=(newValue: Option[Environment]): Unit =
      if simulationEngine.isDefined then onStopClicked()
      simulationEngine = newValue.map(SimulationEngine(_))
      gameMap.environment = simulationEngine.map(_.currentEnvironment)

    override def setPanel(): Unit =
      gamePanel.addToPanel(gameMap, ComponentPosition.WEST)
      gamePanel.addToPanel(hud, ComponentPosition.EAST)
      mainFrame.setPanel(gamePanel)

    override def onStartClicked(): Unit =
      for simulationEngine <- simulationEngine do
        simulationEngine.resume()
        onReceiveEvent[SimulationEvent] from simulationEngine run onEvent

    override def onPauseClicked(): Unit =
      simulationEngine foreach (_.pause())

    override def onResumeClicked(): Unit =
      simulationEngine foreach (_.resume())

    override def onStopClicked(): Unit =
      simulationEngine foreach (_.terminate())
      simulationEngine = None

    override def changeSpeed(newSpeed: Int): Unit =
      simulationEngine foreach (_.changeSpeed(newSpeed))

    private def onEvent(event: SimulationEvent): Unit =
      EventQueue.invokeLater(() =>
        event match
          case SimulationEvent.IterationCompleted(environment) =>
            gameMap.environment = Some(environment)
          case SimulationEvent.SimulationCompleted(environment) =>
            mainFrame.setPanel(EndPanel(environment))
          case SimulationEvent.CountryWonWar(winnerId, loserId, day) =>
            this.hud.writeToConsole(
              s"$loserId has been defeated by $winnerId at day $day"
            )
            this.hud.highlightCountryId(winnerId)
            this.hud.highlightCountryId(loserId)
            this.gameStats.updateEventList(
              winnerId,
              loserId,
              day
            )
      )
