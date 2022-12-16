package it.unibo.warverse.presentation.view

import javax.swing.JPanel
import java.awt.{Component, BorderLayout}

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
