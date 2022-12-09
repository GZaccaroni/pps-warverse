package it.unibo.warverse.presentation.inputs

import it.unibo.warverse.domain.model.Environment

import javax.swing.{JLabel, JMenuItem, JPanel, JPopupMenu, JTextArea}
import java.awt.event.MouseMotionListener
import java.awt.event.MouseEvent
import it.unibo.warverse.domain.model.world.World.Country

import java.awt.{Color, Polygon}
import it.unibo.warverse.domain.model.common.Geometry.Point2D

trait GameMouseMotion extends JPanel with MouseMotionListener:
  def environment: Option[Environment]
  private val popUp: JPopupMenu = JPopupMenu()
  def countryColor(name: String): Color =
    val hash: Int = name.hashCode

    val r: Int = (hash & 0xff0000) >> 16
    val g: Int = (hash & 0x00ff00) >> 8
    val b: Int = hash & 0x0000ff

    Color(r, g, b)
  override def mouseDragged(e: MouseEvent): Unit = mouseMotion(e)

  override def mouseMoved(e: MouseEvent): Unit = mouseMotion(e)

  private def mouseMotion(e: MouseEvent): Unit =
    for environment <- this.environment do
      val mouseX = e.getX
      val mouseY = e.getY
      environment.countries.foreach(country =>
        if country.boundaries.contains(
            Point2D(mouseX, mouseY)
          )
        then
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
      )
