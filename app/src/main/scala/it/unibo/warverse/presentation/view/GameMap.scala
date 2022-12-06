package it.unibo.warverse.presentation.view

import it.unibo.warverse.domain.model.Environment
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
import java.awt.event.MouseEvent
import javax.swing.ToolTipManager

class GameMap extends GameMouseMotion:
  this.requestFocus()
  this.setBackground(Color.BLACK)
  this.setPreferredSize(Dimension(1050, 20))
  this.addMouseMotionListener(this)

  private var _environment: Option[Environment] = None

  def environment: Option[Environment] = _environment
  def environment_=(environment: Option[Environment]): Unit =
    this._environment = environment
    repaint()

  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)
    for environment <- this.environment do
      environment.countries
        .foreach(country =>
          val polygon = Polygon(
            country.boundaries.vertexes.map(_.x.toInt).toArray,
            country.boundaries.vertexes.map(_.y.toInt).toArray,
            country.boundaries.vertexes.length
          )
          val g2d: Graphics2D = g.asInstanceOf[Graphics2D]
          g2d.setColor(countryColor(country.id))
          g2d.fillPolygon(polygon)
          g2d.setColor(Color.WHITE)
          country.armyUnits.foreach(soldier =>
            soldier match
              case _: AreaArmyUnit =>
                g2d.fillRect(
                  soldier.position.x.toInt,
                  soldier.position.y.toInt,
                  3,
                  3
                )
              case _: PrecisionArmyUnit =>
                g2d.fillPolygon(
                  Polygon(
                    Array(
                      soldier.position.x.toInt - 3,
                      soldier.position.x.toInt + 3,
                      soldier.position.x.toInt
                    ),
                    Array(
                      soldier.position.y.toInt + 3,
                      soldier.position.y.toInt + 3,
                      soldier.position.y.toInt
                    ),
                    3
                  )
                )
          )
          g2d.setColor(Color.RED)
          g2d.setStroke(UIConstants.borderRegion)
          g2d.drawPolygon(polygon)
        )
