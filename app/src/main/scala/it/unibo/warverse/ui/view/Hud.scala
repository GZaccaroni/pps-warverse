package it.unibo.warverse.ui.view
import it.unibo.warverse.ui.inputs.{GameMouseMotion}
import java.awt.Graphics
import java.awt.Color
import java.awt.Dimension

class Hud extends GameMouseMotion:

  this.setPreferredSize(new Dimension(350, 20))
  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)
