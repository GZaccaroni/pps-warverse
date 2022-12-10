package it.unibo.warverse

import it.unibo.warverse.presentation.view.MainFrame

import javax.swing.{JFrame, JPanel, RepaintManager, SwingUtilities}
import java.awt.{BorderLayout, Dimension, EventQueue}
import it.unibo.warverse.presentation.view.MenuActions

object Launcher extends App:
  EventQueue.invokeAndWait(() =>
    val mainFrame = MainFrame()
    val menu = MenuActions(mainFrame)
    mainFrame.setPanel(menu)
    mainFrame.pack()
    menu.requestFocus()
    mainFrame.setVisible(true)
    mainFrame.setLocationRelativeTo(null)
  )
