package it.unibo.warverse.presentation.view

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.presentation.inputs.GameMouseMotion
import it.unibo.warverse.presentation.common.UIConstants
import it.unibo.warverse.presentation.common.toColor
import it.unibo.warverse.domain.model.fight.Army.*
import java.awt.{Dimension, Color, Graphics2D, Graphics, Polygon}

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
          val g2d: Graphics2D = g.asInstanceOf[Graphics2D]
          for countryTerritoryPolygon <- country.boundaries.polygons do
            val polygon = Polygon(
              countryTerritoryPolygon.vertexes.map(_.x.toInt).toArray,
              countryTerritoryPolygon.vertexes.map(_.y.toInt).toArray,
              countryTerritoryPolygon.vertexes.length
            )
            g2d.setColor(country.id.toColor)
            g2d.fillPolygon(polygon)
            g2d.setColor(Color.RED)
            g2d.setStroke(UIConstants.borderRegion)
            g2d.drawPolygon(polygon)
          g2d.setColor(Color.WHITE)
          country.armyUnits.foreach(soldier =>
            soldier match
              case _: AreaArmyUnit =>
                g2d.fillRect(
                  soldier.position.x.toInt,
                  soldier.position.y.toInt,
                  6,
                  6
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
        )
