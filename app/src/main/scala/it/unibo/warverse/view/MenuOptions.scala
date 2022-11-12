package it.unibo.warverse.view

import java.awt.Color
import java.awt.Dimension
import java.awt.EventQueue
import java.awt.FontMetrics
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.ArrayList
import java.util.List
import java.util.Map
import javax.swing.AbstractAction
import javax.swing.ActionMap
import javax.swing.InputMap
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.KeyStroke
import javax.swing.UIManager
import javax.swing.UnsupportedLookAndFeelException
import javax.swing.SwingUtilities
import javax.swing.WindowConstants.EXIT_ON_CLOSE
import javax.swing.JComponent
import java.util.HashMap
import javax.swing.JFrame
import java.awt.BorderLayout
import java.awt.geom.RoundRectangle2D
import java.awt.Toolkit
import java.awt.Image
import scala.collection.mutable

class MenuOptions() extends JPanel:

  private var menuBounds =
    scala.collection.mutable.Map[String, RoundRectangle2D]()

  this.setBackground(Color.BLACK)

  this.setPreferredSize(getPreferredSize())

  val im: InputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
  val am: ActionMap = getActionMap

  override def invalidate(): Unit =
    menuBounds = null
    super.invalidate()

  override def getPreferredSize: Dimension =
    Toolkit.getDefaultToolkit.getScreenSize

  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)
    val img: Image = Toolkit.getDefaultToolkit
      .getImage("src/main/scala/it/unibo/warverse/assets/menuBackground.png")
    val g2d: Graphics2D = g.create().asInstanceOf[Graphics2D]
    if menuBounds == null then
      menuBounds = mutable.HashMap[String, RoundRectangle2D]()
      var width = 0
      var totalHeight = 10 * 4
      totalHeight += 5 * (4 - 1)

    g.drawImage(img, 0, 0, this.getSize().width, this.getSize().height, this)
    g2d.dispose()
