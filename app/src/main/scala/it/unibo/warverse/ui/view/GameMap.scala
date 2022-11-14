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
import java.awt.geom.Point2D

class GameMap extends JPanel with MouseMotionListener:
  // 1400 762
  val stateMap: Array[Array[Point2D.Double]] = Array(
    // STATE 1
    Array(
      new Point2D.Double(150, 150),
      new Point2D.Double(250, 100),
      new Point2D.Double(325, 125),
      new Point2D.Double(375, 225),
      new Point2D.Double(450, 250),
      new Point2D.Double(275, 375),
      new Point2D.Double(100, 300)
    ),
    // STATE 2
    Array(
      new Point2D.Double(550, 550),
      new Point2D.Double(650, 500),
      new Point2D.Double(925, 600)
    )
  )
  val start = new Rectangle(0, 0, 200, 200)
  var startHover: Boolean = false
  var popUp: JPopupMenu = new JPopupMenu("TEST")
  val NUM_COLS = 120
  val NUM_ROWS = 240
  var TERRAIN = Array(
    new Color(255,255,0),
    
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
    stateMap.foreach(singleState =>
      var xPoly: Array[Int] = Array()
      var yPoly: Array[Int] = Array()
      singleState.foreach((coordinates: Point2D.Double) =>
        xPoly :+= coordinates.x.toInt
        yPoly :+= coordinates.y.toInt
       
      )
      g2d.setColor(new Color(0,0,255))
      g2d.fillPolygon(xPoly, yPoly, xPoly.length)
    )
  this.setPreferredSize(getPreferredSize())
  this.requestFocus()
