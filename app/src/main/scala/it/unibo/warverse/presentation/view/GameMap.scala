package it.unibo.warverse.presentation.view

import it.unibo.warverse.presentation.inputs.GameMouseMotion
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Graphics
import it.unibo.warverse.domain.model.common.Geometry.Point2D
import it.unibo.warverse.domain.model.common.Geometry.Polygon2D
import it.unibo.warverse.domain.model.world.World.Country
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
import it.unibo.warverse.presentation.controllers.GameStateController
import java.awt.Shape
import it.unibo.warverse.domain.model.Environment

class GameMap extends GameMouseMotion:
  this.requestFocus()
  this.setBackground(Color.BLACK)
  this.setPreferredSize(Dimension(1050, 20))
  var environment: Environment = _

  def setEnvironment(environment: Environment): Unit =
    this.environment = environment
    repaint()

  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)
    this.addMouseMotionListener(this)
    if environment != null then
      super.setCountries(environment.getCountries())
      environment
        .getCountries()
        .foreach(country =>
          val polygon = Polygon()
          val pointList: List[Point2D] = country.boundaries.vertexes
          pointList
            .foreach(point => polygon.addPoint(point.x.toInt, point.y.toInt))
          val g2d: Graphics2D = g.asInstanceOf[Graphics2D]
          g2d.setColor(Color.decode(super.getCountryColor(country.name)))
          g2d.fillPolygon(polygon)
          g2d.setColor(Color.WHITE)
          country.armyUnits.foreach(soldier =>
            if soldier.isInstanceOf[PrecisionArmyUnit] then
              g2d.fillRect(
                soldier.position.x.asInstanceOf[Int],
                soldier.position.y.asInstanceOf[Int],
                3,
                3
              )
            else
              g2d.fillPolygon(
                Polygon(
                  Array(
                    soldier.position.x.asInstanceOf[Int] - 3,
                    soldier.position.x.asInstanceOf[Int] + 3,
                    soldier.position.x.asInstanceOf[Int]
                  ),
                  Array(
                    soldier.position.y.asInstanceOf[Int] + 3,
                    soldier.position.y.asInstanceOf[Int] + 3,
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
