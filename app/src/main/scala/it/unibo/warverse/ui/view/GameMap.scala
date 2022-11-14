package it.unibo.warverse.ui.view

import it.unibo.warverse.ui.inputs.{GameMouseMotion}
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Graphics
import it.unibo.warverse.model.common.Geometry.Point2D
import it.unibo.warverse.model.common.Geometry.Polygon2D
import it.unibo.warverse.model.world.World.Country
import it.unibo.warverse.model.world.World.Citizen
import it.unibo.warverse.model.common.Geometry
import java.awt.Polygon
import scala.language.postfixOps
import it.unibo.warverse.model.fight.Army
import java.awt.BasicStroke
import it.unibo.warverse.ui.common.UIConstants

class GameMap extends GameMouseMotion:
  super.setCountries(UIConstants.testCountries)

  this.requestFocus()

  def getCountryColor(name: String): String =
    String.format("#%X", name.hashCode());

  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)
    this.addMouseMotionListener(this)
    super
      .getCountries()
      .foreach(country =>
        val polygon = new Polygon
        val area = new Polygon
        val pointList: List[Point2D] = country.boundaries.vertexes
        pointList
          .foreach(point => polygon.addPoint(point.x.toInt, point.y.toInt))
        val g2d: Graphics2D = g.asInstanceOf[Graphics2D]
        g2d.setColor(Color.decode(getCountryColor(country.name)))
        g2d.fillPolygon(polygon)
        g2d.setColor(Color.RED)
        g2d.setStroke(UIConstants.borderRegion)
        g2d.drawPolygon(polygon)
      )
