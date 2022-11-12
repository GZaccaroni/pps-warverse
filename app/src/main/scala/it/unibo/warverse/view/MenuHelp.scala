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

class MenuHelp(mainFrame: MainFrame) extends MenuActions(mainFrame):
  setBackGroundImage("src/main/scala/it/unibo/warverse/assets/menuHelp.png")
