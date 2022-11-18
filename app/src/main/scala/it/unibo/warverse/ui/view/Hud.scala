package it.unibo.warverse.ui.view
import it.unibo.warverse.model.world.World
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
import javax.swing.text.Highlighter
import javax.swing.text.Highlighter.HighlightPainter
import javax.swing.text.DefaultHighlighter

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
  val highlighter: Highlighter = console.getHighlighter
  val countries: Array[World.Country] = super.getCountries()
  var text = ""
  private val gameStatus: JScrollPane = new JScrollPane(console)
  gameStatus.setVerticalScrollBarPolicy(22)
  console.setBackground(Color.BLACK)
  console.setForeground(Color.WHITE)
  this.add(gameStatus)

  countries.foreach(country =>
    console.append(
      country.name + " start with " + country.citizens.size + " Citizen, " + country.armyUnits.length + " Army units and " + country.resources + " Resources\n\n"
    )
  )

  console.append("Country1 is in war with Country2\n\n")
  console.append("Country2 is allied with Country3\n\n")

  text = console.getText()
  countries.foreach(country =>
    highlightText(
      text,
      country.name,
      Color.decode(getCountryColor(country.name))
    )
  )
  highlightText(text, "war", Color.RED)
  highlightText(text, "allied", new Color(0, 153, 0))

  speed1Button.addActionListener(_ => console.append("Speed X1\n"))
  speed2Button.addActionListener(_ => console.append("Speed X2\n"))
  speed3Button.addActionListener(_ => console.append("Speed X3\n"))
  startButton.addActionListener(_ => console.append("CLickStart\n"))
  stopButton.addActionListener(_ => console.append("ClickStop\n"))

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

  def highlightText(text: String, name: String, color: Color): Unit =
    val p0: Integer = text.indexOf(name)
    val p1: Integer = p0 + name.length()
    val painter: HighlightPainter =
      new DefaultHighlighter.DefaultHighlightPainter(color)
    highlighter.addHighlight(p0, p1, painter)

  def getCountryColor(name: String): String =
    String.format("#%X", name.hashCode())

  def addJComponents(box: Box, list: List[JComponent]): Unit =
    list.foreach(component => box.add(component))
  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)
