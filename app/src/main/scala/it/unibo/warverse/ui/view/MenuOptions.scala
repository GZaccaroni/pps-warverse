package it.unibo.warverse.ui.view

import it.unibo.warverse.ui.common.UIConstants

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

  this.setBackground(Color.BLACK)
  this.setPreferredSize(getPreferredSize())

  override def invalidate(): Unit =
    super.invalidate()

  override def getPreferredSize: Dimension =
    Toolkit.getDefaultToolkit.getScreenSize

  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)
    val img: Image = Toolkit.getDefaultToolkit
      .getImage(UIConstants.Resources.MainMenuBackground.url)
    val g2d: Graphics2D = g.create().asInstanceOf[Graphics2D]

    g.drawImage(img, 0, 0, this.getSize().width, this.getSize().height, this)
    g2d.dispose()
