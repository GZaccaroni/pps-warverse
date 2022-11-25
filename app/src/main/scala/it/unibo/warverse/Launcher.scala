package it.unibo.warverse

import it.unibo.warverse.presentation.view.{MainFrame, Menu}
import javax.swing.SwingUtilities
import javax.swing.JFrame
import javax.swing.JPanel
import java.awt.Dimension
import java.awt.BorderLayout

object Launcher extends App:
  val mainFrame = new MainFrame()
  val menu = new Menu(mainFrame)
  mainFrame.setPanel(menu)
  mainFrame.pack()
  menu.requestFocus()
  mainFrame.setVisible(true)
