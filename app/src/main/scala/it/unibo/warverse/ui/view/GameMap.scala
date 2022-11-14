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

class GameMap extends GameMouseMotion:
  super.setCountries(
    Array(
      Country(
        "War",
        List(Citizen(Geometry.Point2D(150, 150))),
        List(),
        0.0,
        Polygon2D(
          List(
            Point2D(150, 150),
            Point2D(250, 100),
            Point2D(325, 125),
            Point2D(375, 225),
            Point2D(450, 250),
            Point2D(275, 375),
            Point2D(100, 300)
          )
        )
      ),
      Country(
        "Warverse",
        List(Citizen(Geometry.Point2D(550, 550))),
        List(),
        0.0,
        Polygon2D(
          List(
            Point2D(550, 550),
            Point2D(650, 500),
            Point2D(925, 600)
          )
        )
      )
    )
  )

  this.requestFocus()

  def getCountryColor(name: String): String =
    String.format("#%X", name.hashCode());

  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)
    this.addMouseMotionListener(this)
    var g2d: Graphics2D = g.asInstanceOf[Graphics2D]
    super
      .getCountries()
      .foreach(country =>
        val polygon = new Polygon
        val pointList: List[Point2D] = country.boundaries.vertexes
        pointList
          .foreach(point => polygon.addPoint(point.x.toInt, point.y.toInt))
        g2d.setColor(Color.decode(getCountryColor(country.name)))
        g2d.fillPolygon(polygon)
      )
