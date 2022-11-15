package it.unibo.warverse.ui.view

import javax.swing.JPanel
import javax.swing.BoxLayout
import java.awt.BorderLayout
import java.awt.Dimension

class GamePanel extends JPanel:
  this.setLayout(new BorderLayout)
  this.add(new GameMap, BorderLayout.WEST)
  this.add(new Hud, BorderLayout.EAST)
