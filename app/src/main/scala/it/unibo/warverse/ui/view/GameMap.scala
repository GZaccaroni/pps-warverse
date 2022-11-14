package it.unibo.warverse.ui.view

import javax.swing.JPanel
import java.awt.Color
import java.util.Random
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.Graphics
import java.awt.event.MouseMotionListener
import java.awt.event.MouseEvent
import java.awt.Rectangle
import java.awt.Point
import java.awt.geom.Ellipse2D
import javax.swing.JPopupMenu
import javax.swing.JTextField
import java.awt.Toolkit
import it.unibo.warverse.model.common.Geometry.Point2D
import it.unibo.warverse.model.common.Geometry.Polygon2D
import it.unibo.warverse.model.world.World.Country
import it.unibo.warverse.model.world.World.Citizen
import it.unibo.warverse.model.common.Geometry
import java.awt.Polygon

class GameMap extends JPanel with MouseMotionListener:
  // 1400 762
  val countries: Array[Country] = Array(
    // STATE 1
    new Country(
      "Test1",
      List(new Citizen(new Geometry.Point2D(0, 0))),
      null,
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
    new Country(
      "Test2",
      List(new Citizen(new Geometry.Point2D(0, 0))),
      null,
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
  val start = new Rectangle(0, 0, 200, 200)
  var startHover: Boolean = false
  var popUp: JPopupMenu = new JPopupMenu("TEST")
  val NUM_COLS = 120
  val NUM_ROWS = 240
  var TERRAIN = Array(
    new Color(255, 255, 0)
  )
  val terrainGrid: Array[Array[Color]] =
    Array.ofDim[Color](NUM_ROWS + 1, NUM_COLS + 1)

  override def getPreferredSize(): Dimension =
    return Toolkit.getDefaultToolkit().getScreenSize()
  override def mouseDragged(e: MouseEvent): Unit = mouseMoved(e)

  override def mouseMoved(e: MouseEvent): Unit =
    val mx = e.getX()

  override def paintComponent(g: Graphics) =
    super.paintComponent(g)
    this.addMouseMotionListener(this)
    g.clearRect(0, 0, getWidth(), getHeight())
    val rectWidth: Int = 5
    val rectHeight: Int = 5
    var g2d: Graphics2D = g.asInstanceOf[Graphics2D]
    var xPoly: Array[Int] = Array()
    var yPoly: Array[Int] = Array()
    countries.foreach(country =>
      val polygon = new Polygon
      val pointList = country.boundaries.vertexes
      pointList.foreach(point => polygon.addPoint(point.x.toInt, point.y.toInt))
      g2d.fillPolygon(polygon)
    )

  this.setPreferredSize(getPreferredSize())
  this.requestFocus()
