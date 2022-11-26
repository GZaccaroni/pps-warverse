package it.unibo.warverse.presentation.inputs

import it.unibo.warverse.presentation.view.{
  MainFrame,
  MenuActions,
  MenuHelp,
  MenuOptions,
  GameMap,
  Hud,
  GamePanel
}
import javax.swing.AbstractAction
import java.awt.event.ActionEvent
import javax.swing.JPanel
import it.unibo.warverse.presentation.controllers.GameStateController

trait EnterAction extends AbstractAction:
  def panel: MenuActions
  def mainFrame: MainFrame

object EnterAction:
  def apply(
    panel: MenuActions,
    mainFrame: MainFrame
  ): EnterAction = EnterActionImpl(panel, mainFrame)

  private class EnterActionImpl(
    override val panel: MenuActions,
    override val mainFrame: MainFrame
  ) extends EnterAction:

    override def actionPerformed(e: ActionEvent): Unit =
      panel.getMenuItems match
        case "Start Game" => GameStateController(mainFrame).setPanel()
        case "Options"    => panel.setNewPanel(MenuOptions())
        case "Help"       => panel.setNewPanel(MenuHelp(mainFrame))
        case "Exit"       => System.exit(0)
        case _            =>
      panel.repaint()
      panel.invalidate()
