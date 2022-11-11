package it.unibo.warverse

import javax.swing.AbstractAction
import java.awt.event.ActionEvent
import javax.swing.JPanel
import it.unibo.warverse.Menu
import it.unibo.warverse.MenuOptions
import it.unibo.warverse.MenuHelp

class EnterAction(
  panel: MenuActions,
  setNewPanel: JPanel => Unit,
) extends AbstractAction:

  override def actionPerformed(e: ActionEvent): Unit =
    panel.getMenuItems() match
          //case "Start Game" => setNewPanel(new Map())
          case "Options" => setNewPanel(new MenuOptions())
          case "Help" => setNewPanel(new MenuHelp())
          case "Exit" => System.exit(0)
          case _ =>
    panel.repaint()
    panel.invalidate()
