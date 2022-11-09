package it.unibo.warverse

import javax.swing.JFrame
import javax.swing.WindowConstants.EXIT_ON_CLOSE
import javax.swing.JPanel
import java.awt.Toolkit
import java.awt.BorderLayout
import javax.swing.ImageIcon
import java.awt.Image


class MainFrame extends JFrame:
  this.setName("Warverse Simulator")
  this.setDefaultCloseOperation(EXIT_ON_CLOSE);
  this.pack();
  this.setLocationRelativeTo(null);

  def setPanel(panel: JPanel): Unit =
    this.getContentPane().removeAll()
    this.add(panel, BorderLayout.CENTER)
