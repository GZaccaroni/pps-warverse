package it.unibo.warverse.ui.view
import it.unibo.warverse.ui.inputs.{GameMouseMotion}
import java.awt.Graphics
import java.awt.Color
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.border.Border
import java.awt.Component
import java.awt.Insets
import java.awt.event.ActionListener
import java.awt.Event
import javax.swing.Box
import javax.swing.JComponent

class Hud extends GameMouseMotion:
  this.setPreferredSize(new Dimension(350, 20))
  val startButton = new JButton("Start");
  startButton.setForeground(Color.BLUE);
  val stopButton = new JButton("Stop");
  stopButton.setForeground(Color.RED);
  val speed1Button = new JButton("X1");
  val speed2Button = new JButton("X2");
  val speed3Button = new JButton("X3");
  val verticalContainer = Box.createVerticalBox()
  val firstButtonsRow = Box.createHorizontalBox()
  val secondButtonsRow = Box.createHorizontalBox()
  speed1Button.addActionListener(e => println("Speed1"))
  speed2Button.addActionListener(e => println("Speed2"))
  speed3Button.addActionListener(e => println("Speed3"))
  startButton.addActionListener(e => println("CLickStart"))
  stopButton.addActionListener(e => println("ClickStop"))

  addJComponents(firstButtonsRow, List(startButton, stopButton))
  addJComponents(
    secondButtonsRow,
    List(speed1Button, speed2Button, speed3Button)
  )
  addJComponents(
    verticalContainer,
    List(firstButtonsRow, secondButtonsRow)
  )
  this.add(verticalContainer)

  def addJComponents(box: Box, list: List[JComponent]): Unit =
    list.foreach(component => box.add(component))
  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)
