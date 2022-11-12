package it.unibo.warverse.inputs

import javax.swing.AbstractAction
import java.awt.event.ActionEvent
import javax.swing.JPanel
import it.unibo.warverse.view.MenuOptions
import it.unibo.warverse.view.MenuActions
import it.unibo.warverse.view.MainFrame
import it.unibo.warverse.view.MenuHelp

class EnterAction(
  panel: MenuActions,
  setNewPanel: JPanel => Unit,
  mainFrame: MainFrame
) extends AbstractAction:

  override def actionPerformed(e: ActionEvent): Unit =
    panel.getMenuItems match
      case "Options" => setNewPanel(new MenuOptions())
      case "Help"    => setNewPanel(new MenuHelp(mainFrame))
      case "Exit"    => System.exit(0)
      case _         =>
    panel.repaint()
    panel.invalidate()
