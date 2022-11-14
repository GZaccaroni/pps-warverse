package it.unibo.warverse.ui.inputs

import javax.swing.JPanel
import java.awt.event.MouseMotionListener
import java.awt.event.MouseEvent
import it.unibo.warverse.model.world.World.Country
import java.awt.Polygon
import it.unibo.warverse.model.common.Geometry.Point2D
import javax.swing.JPopupMenu
import javax.swing.JLabel

abstract class GameMouseMotion extends JPanel with MouseMotionListener:
  private var countries: Array[Country] = _

  var popUp: JPopupMenu = new JPopupMenu("")

  protected var previousHoverCountry: String = ""

  def setCountries(countries: Array[Country]): Unit =
    this.countries = countries

  def getCountries(): Array[Country] = this.countries

  override def mouseDragged(e: MouseEvent): Unit = mouseMotion(e)

  override def mouseMoved(e: MouseEvent): Unit = mouseMotion(e)

  def mouseMotion(e: MouseEvent): Unit =
    val mouseX = e.getX
    val mouseY = e.getY
    countries.foreach(country =>
      val polygon = new Polygon
      val pointList: List[Point2D] = country.boundaries.vertexes

      pointList.foreach(point => polygon.addPoint(point.x.toInt, point.y.toInt))
      if polygon.contains(mouseX, mouseY)
      then
        if country.name != previousHoverCountry
        then
          popUp.setVisible(false)
          popUp.removeAll()
          popUp.add(new JLabel("Country: " + country.name))
          popUp.show(this, mouseX, mouseY)
          popUp.setVisible(true)
          this.previousHoverCountry = country.name
    )
