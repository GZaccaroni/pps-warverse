package it.unibo.warverse.presentation.view

import javax.swing.WindowConstants.EXIT_ON_CLOSE
import javax.swing.{JPanel, JFrame}
import java.awt.BorderLayout

trait MainFrame extends JFrame:
  def setPanel(panel: JPanel): Unit

object MainFrame:
  def apply(): MainFrame = MainFrameImpl()

  private class MainFrameImpl extends MainFrame:
    this.setName("Warverse Simulator")
    this.setDefaultCloseOperation(EXIT_ON_CLOSE)
    this.setResizable(false)
    this.setLocationRelativeTo(null)
    this.pack()

    override def setPanel(panel: JPanel): Unit =
      this.getContentPane.removeAll()
      this.add(panel, BorderLayout.CENTER)
      this.validate()
