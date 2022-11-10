package it.unibo.warverse
import javax.swing.SwingUtilities
import javax.swing.JFrame
import javax.swing.JPanel
import java.awt.Dimension
import java.awt.BorderLayout

@main def launch(): Unit =
  val mainFrame = new MainFrame()
  val menu = new Menu(mainFrame)
  mainFrame.setPanel(menu)
  mainFrame.pack()
  mainFrame.setVisible(true)

