package it.unibo.warverse.ui.view
import it.unibo.warverse.model.world.World
import it.unibo.warverse.ui.inputs.GameMouseMotion
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JFileChooser
import java.io.File
import java.awt.Color
import javax.swing.Box
import javax.swing.JTextArea
import javax.swing.JScrollPane
import java.awt.Insets
import javax.swing.JComponent
import it.unibo.warverse.ui.common.JsonConfigParser
import java.awt.Graphics
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
import it.unibo.warverse.controller.GameStateController
import scala.io.Source
import javax.swing.JOptionPane
import it.unibo.warverse.model.world.Relations
import it.unibo.warverse.model.world.Relations.InterstateRelations

class Hud extends GameMouseMotion:
  super.setCountries(UIConstants.testCountries)
  this.setPreferredSize(Dimension(350, 20))
  val uploadConfig = JButton("Upload Configuration")
  val fileChooser = JFileChooser()
  fileChooser.setCurrentDirectory(
    File(
      System.getProperty("user.home") + System.getProperty(
        "file.separator"
      ) + "Desktop"
    )
  )
  uploadConfig.addActionListener(_ => uploadJson())
  val startButton = JButton("Start")
  startButton.setForeground(Color.BLUE)
  val stopButton = JButton("Stop")
  stopButton.setForeground(Color.RED)
  val speed1Button = JButton("X1")
  val speed2Button = JButton("X2")
  val speed3Button = JButton("X3")
  private val verticalContainer = Box.createVerticalBox()
  private val firstButtonsRow = Box.createHorizontalBox()
  private val secondButtonsRow = Box.createHorizontalBox()
  private val console: JTextArea = JTextArea(25, 25)
  this.console.setMargin(Insets(10, 10, 10, 10))
  this.console.setEditable(false)
  this.console.setLineWrap(true)
  this.console.setWrapStyleWord(true)
  val highlighter: Highlighter = console.getHighlighter
  var countries: Array[World.Country] = _
  var text = ""
  var controller: GameStateController = _
  val relations = Relations
  private val gameStatus: JScrollPane = JScrollPane(console)
  gameStatus.setVerticalScrollBarPolicy(22)
  this.add(uploadConfig)
  console.setBackground(Color.BLACK)
  console.setForeground(Color.WHITE)
  this.add(gameStatus)

  speed1Button.addActionListener(_ => console.append("Speed X1\n"))
  speed2Button.addActionListener(_ => console.append("Speed X2\n"))
  speed3Button.addActionListener(_ => console.append("Speed X3\n"))
  startButton.addActionListener(_ => controller.startClicked())
  stopButton.addActionListener(_ => controller.stopClicked())

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

  def updateConsole(relations: InterstateRelations): Unit =
    countries.foreach(country =>
      console.append(
        country.name + " start with " + country.citizens.size + " Citizen, " + country.armyUnits.length + " Army units and " + country.resources + " Resources\n\n"
      )
    )

    countries.foreach(country =>
      relations
        .getAllies(country)
        .foreach(ally =>
          console.append(country.name + " is allied with " + ally.name + "\n\n")
        )
    )
    countries.foreach(country =>
      relations
        .getEnemies(country)
        .foreach(enemy =>
          console.append(country.name + " is in war with " + enemy.name + "\n\n")
        )
    )
    text = console.getText()
    countries.foreach(country =>
      highlightText(
        text,
        country.name,
        Color.decode(getCountryColor(country.name))
      )
    )

    highlightText(text, "allied", Color(0, 153, 0))
    highlightText(text, "war", Color.RED)


  def setController(controller: GameStateController): Unit =
    this.controller = controller

  def highlightText(text: String, name: String, color: Color): Unit =
    var c: Integer = 0
    while text.indexOf(name, c) != -1 do
      val p0: Integer = text.indexOf(name, c)
      val p1: Integer = p0 + name.length()
      val painter: HighlightPainter =
        DefaultHighlighter.DefaultHighlightPainter(color)
      highlighter.addHighlight(p0, p1, painter)
      c = p1

  def getCountryColor(name: String): String =
    String.format("#%X", name.hashCode())

  def addJComponents(box: Box, list: List[JComponent]): Unit =
    list.foreach(component => box.add(component))

  def getExtensionByStringHandling(filename: String): Boolean =
    filename.split("\\.").last == "json"

  def uploadJson(): Unit =
    fileChooser.showOpenDialog(this)
    val file = fileChooser.getSelectedFile
    if getExtensionByStringHandling(file.getName) then
      val jsonFile = Source
        .fromFile(file)
        .mkString

      val jsonConfigParser: JsonConfigParser = JsonConfigParser(jsonFile)
      try
        val resJson = jsonConfigParser.getConfig
        val allAlliance = jsonConfigParser.getStringAlliance
        val allEnemies = jsonConfigParser.getStringEnemies
        this.controller.setAllCountries(jsonConfigParser.getConfigList)
        this.controller.setRelationship(allAlliance, allEnemies)
        super.setCountries(resJson)
        this.countries = resJson
        updateConsole(this.controller.getRelationship())
        JOptionPane.showMessageDialog(
          null,
          "Configuration uploaded successfully."
        )
      catch
        case _: NullPointerException =>
          println("Configuration File have some errors.")

  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)
