package it.unibo.warverse

import java.awt.EventQueue
import it.unibo.warverse.presentation.view.{MenuActions, MainFrame}

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
