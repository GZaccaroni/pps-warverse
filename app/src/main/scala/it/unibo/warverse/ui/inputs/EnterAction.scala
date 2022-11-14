package it.unibo.warverse.ui.inputs

import it.unibo.warverse.ui.view.{MainFrame, MenuActions, MenuHelp, MenuOptions, GameMap}

import javax.swing.AbstractAction
import java.awt.event.ActionEvent
import javax.swing.JPanel

class EnterAction(
  panel: MenuActions,
  setNewPanel: JPanel => Unit,
  mainFrame: MainFrame
) extends AbstractAction:

  override def actionPerformed(e: ActionEvent): Unit =
    panel.getMenuItems match
      case "Start Game" => setNewPanel(new GameMap())
      case "Options" => setNewPanel(new MenuOptions())
      case "Help"    => setNewPanel(new MenuHelp(mainFrame))
      case "Exit"    => System.exit(0)
      case _         =>
    panel.repaint()
    panel.invalidate()
