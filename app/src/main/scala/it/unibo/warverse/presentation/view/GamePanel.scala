package it.unibo.warverse.presentation.view

import javax.swing.JPanel
import javax.swing.BoxLayout
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Component

class GamePanel extends JPanel:
  this.setLayout(new BorderLayout)

  def addToPanel(component: Component, constraints: Object): Unit =
    this.add(component, constraints)
