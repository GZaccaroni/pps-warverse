package it.unibo.warverse.ui.view
import it.unibo.warverse.ui.inputs.GameMouseMotion
import java.awt.Graphics
import java.awt.Color
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.border.Border
import java.awt.Component
import java.awt.Insets
import java.awt.event.ActionListener
import javax.swing.Box
import javax.swing.JComponent
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.BorderFactory
import it.unibo.warverse.ui.common.UIConstants
import javax.swing.JTextPane
import javax.swing.border.EmptyBorder
import javax.swing.text.StyleContext
import javax.swing.text.AttributeSet
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants

class Hud extends GameMouseMotion:
  super.setCountries(UIConstants.testCountries)
  this.setPreferredSize(new Dimension(350, 20))
  val startButton = new JButton("Start")
  startButton.setForeground(Color.BLUE)
  val stopButton = new JButton("Stop")
  stopButton.setForeground(Color.RED)
  val speed1Button = new JButton("X1")
  val speed2Button = new JButton("X2")
  val speed3Button = new JButton("X3")
  private val verticalContainer = Box.createVerticalBox()
  private val firstButtonsRow = Box.createHorizontalBox()
  private val secondButtonsRow = Box.createHorizontalBox()
  private val console: JTextArea = new JTextArea(25, 25)
  this.console.setMargin(new Insets(10, 10, 10, 10))
  this.console.setEditable(false)
  this.console.setLineWrap(true)
  this.console.setWrapStyleWord(true)
  var text = new JTextPane();
  text.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)))
  private val gameStatus: JScrollPane = new JScrollPane(console)
  gameStatus.setVerticalScrollBarPolicy(22)

  this.add(gameStatus)
  
  super
    .getCountries()
    .foreach(country =>
      console.append(
        country.name + " start with " + country.citizens.length + " Citizen, " + country.armyUnits.length + " Army units and " + country.resources + " Resources\n\n"
      )
    )

  speed1Button.addActionListener(e => console.append("Speed X1\n"))
  speed2Button.addActionListener(e => console.append("Speed X2\n"))
  speed3Button.addActionListener(e => console.append("Speed X3\n"))
  startButton.addActionListener(e => console.append("CLickStart\n"))
  stopButton.addActionListener(e => console.append("ClickStop\n"))

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
