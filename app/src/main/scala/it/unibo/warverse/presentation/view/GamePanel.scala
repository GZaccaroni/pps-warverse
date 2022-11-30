package it.unibo.warverse.presentation.view

import javax.swing.JPanel
import javax.swing.BoxLayout
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Component

enum GuiEnum:
  case WEST, EAST, NORTH, SOUTH

trait GamePanel extends JPanel:
  def addToPanel(component: Component, constraints: Object): Unit

object GamePanel:
  def apply(): GamePanel = GamePanelImpl()

  private class GamePanelImpl extends GamePanel:
    this.setLayout(BorderLayout())

    override def addToPanel(component: Component, constraints: Object): Unit =
      constraints match
        case GuiEnum.WEST => this.add(component, BorderLayout.WEST)
        case GuiEnum.EAST => this.add(component, BorderLayout.EAST)
      
