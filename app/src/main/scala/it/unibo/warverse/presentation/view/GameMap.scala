package it.unibo.warverse.presentation.view

import it.unibo.warverse.presentation.inputs.GameMouseMotion
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Graphics
import it.unibo.warverse.domain.model.common.Geometry.Point2D
import it.unibo.warverse.domain.model.common.Geometry.Polygon2D
import it.unibo.warverse.domain.model.world.World.Country
import it.unibo.warverse.domain.model.world.World.Citizen
import it.unibo.warverse.domain.model.common.Geometry
import java.awt.Polygon
import scala.language.postfixOps
import it.unibo.warverse.domain.model.fight.Army
import java.awt.BasicStroke
import it.unibo.warverse.presentation.common.UIConstants
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Toolkit
import it.unibo.warverse.domain.model.fight.Army.*

class GameMap extends GameMouseMotion:
  super.setCountries(UIConstants.testCountries)
  this.requestFocus()
  this.setBackground(Color.BLACK)
  this.setPreferredSize(Dimension(1050, 20))

  def getCountryColor(name: String): String =
    String.format("#%X", name.hashCode())

  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)
    this.addMouseMotionListener(this)
    super.getCountries
      .foreach(country =>
        val polygon = Polygon()
        val pointList: List[Point2D] = country.boundaries.vertexes
        pointList
          .foreach(point => polygon.addPoint(point.x.toInt, point.y.toInt))
        val g2d: Graphics2D = g.asInstanceOf[Graphics2D]
        g2d.setColor(Color.decode(getCountryColor(country.name)))
        g2d.fillPolygon(polygon)
        // Drawing Soldiers
        g2d.setColor(Color.WHITE)
        country.armyUnits.foreach(soldier =>
          if soldier.isInstanceOf[PrecisionArmyUnit] then
            g2d.fillRect(
              soldier.position.x.asInstanceOf[Int],
              soldier.position.y.asInstanceOf[Int],
              5,
              5
            )
          else
            g2d.fillPolygon(
              Polygon(
                Array(
                  soldier.position.x.asInstanceOf[Int] - 5,
                  soldier.position.x.asInstanceOf[Int] + 5,
                  soldier.position.x.asInstanceOf[Int]
                ),
                Array(
                  soldier.position.y.asInstanceOf[Int] + 5,
                  soldier.position.y.asInstanceOf[Int] + 5,
                  soldier.position.y.asInstanceOf[Int]
                ),
                3
              )
            )
        )
        g2d.setColor(Color.RED)
        g2d.setStroke(UIConstants.borderRegion)
        g2d.drawPolygon(polygon)
      )
