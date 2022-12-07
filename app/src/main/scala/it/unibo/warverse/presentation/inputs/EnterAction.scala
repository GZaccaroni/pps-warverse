package it.unibo.warverse.presentation.inputs

import it.unibo.warverse.presentation.view.{
  GameMap,
  GamePanel,
  Hud,
  MainFrame,
  MenuActions,
  MenuHelp,
  MenuOptions
}

import javax.swing.AbstractAction
import java.awt.event.ActionEvent
import javax.swing.JPanel
import it.unibo.warverse.presentation.controllers.GameStateController
import it.unibo.warverse.presentation.model.MenuItems.MainMenuItem

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
      panel.selectedItem match
        case MainMenuItem.StartGame => GameStateController(mainFrame).setPanel()
        case MainMenuItem.Help      => panel.setNewPanel(MenuHelp(mainFrame))
        case MainMenuItem.Exit      => System.exit(0)
        case _                      =>
      panel.repaint()
      panel.invalidate()
