package it.unibo.warverse.presentation.inputs

import javax.swing.JPanel
import java.awt.event.MouseMotionListener
import java.awt.event.MouseEvent
import it.unibo.warverse.domain.model.world.World.Country
import java.awt.Polygon
import it.unibo.warverse.domain.model.common.Geometry.Point2D
import javax.swing.JPopupMenu
import javax.swing.JLabel
import javax.swing.JMenuItem

trait GameMouseMotion extends JPanel with MouseMotionListener:
  private var countries: List[Country] = _
  val popUp: JPopupMenu = JPopupMenu()

  def getCountryColor(name: String): String =
    String.format("#%X", name.hashCode())

  def setCountries(countries: List[Country]): Unit =
    this.countries = countries

  def getCountries: List[Country] = this.countries

  override def mouseDragged(e: MouseEvent): Unit = mouseMotion(e)

  override def mouseMoved(e: MouseEvent): Unit = mouseMotion(e)

  def mouseMotion(e: MouseEvent): Unit =
    val mouseX = e.getX
    val mouseY = e.getY
    if countries != null then
      countries.foreach(country =>
        val polygon = Polygon()
        val pointList: List[Point2D] = country.boundaries.vertexes

        pointList.foreach(point =>
          polygon.addPoint(point.x.toInt, point.y.toInt)
        )
        if polygon.contains(
            mouseX,
            mouseY
          )
        then
          popUp.setVisible(false)
          popUp.removeAll()
          popUp.add(JMenuItem("Country: " + country.name))
          popUp.add(JMenuItem("Army Units: " + country.armyUnits.size))
          popUp.add(JMenuItem("Citizens: " + country.citizens))
          popUp.add(JMenuItem("Resources: " + country.resources))
          popUp.show(this, mouseX, mouseY)
          popUp.setVisible(true)
      )
