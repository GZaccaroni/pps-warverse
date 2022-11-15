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
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Toolkit
import javax.imageio.ImageIO
import java.io.File
import java.awt.image.BufferedImage

class GameMap extends GameMouseMotion with Runnable:
  super.setCountries(UIConstants.testCountries)
  this.requestFocus()
  this.setBackground(Color.BLACK)
  this.setPreferredSize(new Dimension(1050, 20))

  var gameThread: Thread = _
  this.startGameLoop()

  def startGameLoop(): Unit =
    gameThread = new Thread(this)
    gameThread.start()

  override def run(): Unit =
    val timeFrame = 1000000000.0 / 120
    var lastFrame = System.nanoTime
    val now = System.nanoTime
    var frames = 0
    var lastCheck = System.currentTimeMillis
    while true do
      if now - lastFrame >= timeFrame then
        // TODO call Update position method
        this.repaint()
        lastFrame = now
        frames = frames + 1
      if System.currentTimeMillis() - lastCheck >= 1000 then
        lastCheck = System.currentTimeMillis()
        println("FPS " + frames)
        frames = 0

  // TODO Update Body position
  def updatePosition(countries: Array[Country]): Unit = ???
  def getCountryColor(name: String): String =
    String.format("#%X", name.hashCode())

  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)
    this.addMouseMotionListener(this)
    super
      .getCountries()
      .foreach(country =>
        val polygon = new Polygon
        val pointList: List[Point2D] = country.boundaries.vertexes
        pointList
          .foreach(point => polygon.addPoint(point.x.toInt, point.y.toInt))
        val g2d: Graphics2D = g.asInstanceOf[Graphics2D]
        g2d.setColor(Color.decode(getCountryColor(country.name)))
        g2d.fillPolygon(polygon)
        //Drawing Soldiers
        g2d.setColor(Color.WHITE)
        country.armyUnits.foreach(soldier =>
          if(soldier.name == "Soldato") then
            g2d.fillRect(soldier.position.x.asInstanceOf[Int], soldier.position.y.asInstanceOf[Int], 5, 5)
          else
            g2d.fillPolygon(new Polygon(
              Array(soldier.position.x.asInstanceOf[Int]-5, soldier.position.x.asInstanceOf[Int]+5, soldier.position.x.asInstanceOf[Int]), 
              Array(soldier.position.y.asInstanceOf[Int]+5, soldier.position.y.asInstanceOf[Int]+5, soldier.position.y.asInstanceOf[Int]), 
              3
              )
        )
        )
        g2d.setColor(Color.RED)
        g2d.setStroke(UIConstants.borderRegion)
        g2d.drawPolygon(polygon)
      )
