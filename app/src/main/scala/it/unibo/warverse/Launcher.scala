package it.unibo.warverse

import it.unibo.warverse.presentation.view.MainFrame
import javax.swing.SwingUtilities
import javax.swing.JFrame
import javax.swing.JPanel
import java.awt.Dimension
import java.awt.BorderLayout
import it.unibo.warverse.presentation.view.MenuActions

object Launcher extends App:
  val mainFrame = MainFrame()
  val menu = MenuActions(mainFrame)
  mainFrame.setPanel(menu)
  mainFrame.pack()
  menu.requestFocus()
  mainFrame.setVisible(true)
  mainFrame.setLocationRelativeTo(null)
