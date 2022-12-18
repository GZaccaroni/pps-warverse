package it.unibo.warverse.presentation.inputs

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.common.Geometry.Point2D
import javax.swing.{JMenuItem, JPanel, JPopupMenu}
import java.awt.event.{MouseMotionListener, MouseEvent}
import java.awt.Color

trait GameMouseMotion extends JPanel with MouseMotionListener:
  def environment: Option[Environment]
  private val popUp: JPopupMenu = JPopupMenu()
  override def mouseDragged(e: MouseEvent): Unit = mouseMotion(e)

  override def mouseMoved(e: MouseEvent): Unit = mouseMotion(e)

  private def mouseMotion(e: MouseEvent): Unit =
    val mouseX = e.getX
    val mouseY = e.getY
    for
      environment <- this.environment
      country <- environment.countries
      if country.boundaries.contains(Point2D(mouseX, mouseY))
    do
      popUp.setVisible(false)
      popUp.removeAll()
      popUp.add(JMenuItem(s"Country: ${country.name}"))
      popUp.add(JMenuItem(s"Army Units: ${country.armyUnits.size}"))
      popUp.add(JMenuItem(s"Citizens: ${country.citizens}"))
      popUp.add(
        JMenuItem(
          s"Resources: ${String.format("%.02f", country.resources)}"
        )
      )
      popUp.show(this, mouseX, mouseY)
      popUp.setVisible(true)
