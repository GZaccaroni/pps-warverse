package it.unibo.warverse.presentation.view

import javax.swing.JPanel
import java.awt.{Component, BorderLayout}

enum ComponentPosition:
  case WEST, EAST, NORTH, SOUTH

  def toBorderLayoutPosition: String =
    this match
      case ComponentPosition.NORTH => BorderLayout.NORTH
      case ComponentPosition.EAST  => BorderLayout.EAST
      case ComponentPosition.WEST  => BorderLayout.WEST
      case ComponentPosition.SOUTH => BorderLayout.SOUTH

trait GamePanel extends JPanel:
  def addToPanel(component: Component, constraints: ComponentPosition): Unit

object GamePanel:
  def apply(): GamePanel = GamePanelImpl()

  private class GamePanelImpl extends GamePanel:
    this.setLayout(BorderLayout())

    override def addToPanel(
      component: Component,
      constraints: ComponentPosition
    ): Unit =
      this.add(component, constraints.toBorderLayoutPosition)
