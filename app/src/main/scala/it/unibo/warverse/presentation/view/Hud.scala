package it.unibo.warverse.presentation.view
import it.unibo.warverse.domain.model.world.World
import it.unibo.warverse.presentation.inputs.GameMouseMotion

import java.awt.Dimension
import javax.swing.{
  BorderFactory,
  Box,
  JButton,
  JComponent,
  JFileChooser,
  JOptionPane,
  JPanel,
  JScrollPane,
  JTextArea,
  JTextPane
}
import java.io.File
import java.awt.Color
import java.awt.Insets
import java.awt.Graphics
import it.unibo.warverse.presentation.common.UIConstants

import javax.swing.border.EmptyBorder
import javax.swing.text.StyleContext
import javax.swing.text.AttributeSet
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.Highlighter
import javax.swing.text.Highlighter.HighlightPainter
import javax.swing.text.DefaultHighlighter
import it.unibo.warverse.presentation.controllers.GameStateController
import it.unibo.warverse.data.data_sources.simulation_config.SimulationConfigDataSource
import it.unibo.warverse.domain.model.{Environment, SimulationConfig}

import scala.io.Source
import it.unibo.warverse.domain.model.world.Relations
import it.unibo.warverse.domain.model.world.Relations.InterstateRelations

class Hud extends JPanel:
  this.setPreferredSize(Dimension(350, 20))
  private val uploadConfig = JButton("Upload Configuration")
  private val fileChooser = JFileChooser()
  fileChooser.setCurrentDirectory(
    File(
      System.getProperty("user.home") + System.getProperty(
        "file.separator"
      ) + "Desktop"
    )
  )
  uploadConfig.addActionListener(_ => uploadJson())
  private val toggleSimulationButton = JButton("Start")
  toggleSimulationButton.setForeground(Color.BLUE)
  private val stopButton = JButton("Stop")
  stopButton.setForeground(Color.RED)
  private val speed1Button = JButton("X1")
  private val speed2Button = JButton("X2")
  private val speed3Button = JButton("X3")
  private val verticalContainer = Box.createVerticalBox()
  private val firstButtonsRow = Box.createHorizontalBox()
  private val secondButtonsRow = Box.createHorizontalBox()
  private val console: JTextArea = JTextArea(25, 25)
  this.console.setMargin(Insets(10, 10, 10, 10))
  this.console.setEditable(false)
  this.console.setLineWrap(true)
  this.console.setWrapStyleWord(true)
  val highlighter: Highlighter = console.getHighlighter
  var controller: GameStateController = _
  private val gameStatus: JScrollPane = JScrollPane(console)
  gameStatus.setVerticalScrollBarPolicy(22)
  this.add(uploadConfig)
  console.setBackground(Color.BLACK)
  console.setForeground(Color.WHITE)
  this.add(gameStatus)

  toggleSimulationButton.addActionListener(_ =>
    (toggleSimulationButton.getText, controller.simulationConfig) match
      case ("Start", Some(_)) =>
        controller.onStartClicked()
        toggleSimulationButton.setText("Pause")
      case ("Pause", _) =>
        controller.onPauseClicked()
        toggleSimulationButton.setText("Resume")
      case ("Resume", _) =>
        controller.onResumeClicked()
        toggleSimulationButton.setText("Pause")
      case _ =>
        JOptionPane.showMessageDialog(
          null,
          "Choose a simulation config before starting simulation."
        )
  )
  stopButton.addActionListener(_ => controller.onStopClicked())
  
  speed1Button.setEnabled(false)
  speed1Button.addActionListener(_ =>
    writeToConsole("Speed set to X1")
    controller.changeSpeed(1)
    enableSpeed(false, true, true)
  )
  speed2Button.addActionListener(_ =>
    writeToConsole("Speed set to X2")
    controller.changeSpeed(2)
    enableSpeed(true, false, true)
  )
  speed3Button.addActionListener(_ =>
    writeToConsole("Speed set to X3")
    controller.changeSpeed(3)
    enableSpeed(true, true, false)
  )

  addJComponents(firstButtonsRow, List(toggleSimulationButton, stopButton))
  addJComponents(
    secondButtonsRow,
    List(speed1Button, speed2Button, speed3Button)
  )
  addJComponents(
    verticalContainer,
    List(firstButtonsRow, secondButtonsRow)
  )
  this.add(verticalContainer)

  def setController(controller: GameStateController): Unit =
    this.controller = controller

  private def addJComponents(box: Box, list: Seq[JComponent]): Unit =
    list.foreach(component => box.add(component))

  private def getExtensionByStringHandling(filename: String): Boolean =
    filename.split("\\.").last == "json"

  private def uploadJson(): Unit =
    fileChooser.showOpenDialog(this)
    val file = fileChooser.getSelectedFile
    if getExtensionByStringHandling(file.getName) then

      val jsonConfigParser =
        SimulationConfigDataSource(file, SimulationConfigDataSource.Format.Json)
      try
        val simulationConfig = jsonConfigParser.simulationConfig
        displayInitialSimulationConfig(simulationConfig)
        controller.simulationConfig = Some(simulationConfig)
        JOptionPane.showMessageDialog(
          null,
          "Configuration uploaded successfully."
        )
      catch
        case _: NullPointerException =>
          println("Configuration File have some errors.")

  private def displayInitialSimulationConfig(
    simulationConfig: SimulationConfig
  ): Unit =
    console.setText("")
    simulationConfig.countries.foreach(country =>
      writeToConsole(
        country.name + " starts with " + country.citizens + " citizen, " + country.armyUnits.length + " army units and " + String
          .format("%.02f", country.resources) + " resources."
      )
    )

    simulationConfig.countries.foreach(country =>
      simulationConfig.interstateRelations
        .countryAllies(country.id)
        .foreach(allyId =>
          val ally = simulationConfig.countries.find(_.id == allyId).get;
          writeToConsole(country.name + " is allied with " + ally.name)
        )
      simulationConfig.interstateRelations
        .countryEnemies(country.id)
        .foreach(enemyId =>
          val enemy = simulationConfig.countries.find(_.id == enemyId).get;
          writeToConsole(
            country.name + " is in war with " + enemy.name
          )
        )
    )
    simulationConfig.countries.foreach(country =>
      highlightText(
        name = country.name,
        countryColor(country.id)
      )
    )
    highlightText(name = "allied", Color(0, 153, 0))
    highlightText(name = "war", Color.RED)
    writeToConsole("Default speed is set to X1")

  private def highlightText(text: String = console.getText, name: String, color: Color): Unit =
    var c: Int = 0
    val painter: HighlightPainter =
      DefaultHighlighter.DefaultHighlightPainter(color)
    while text.indexOf(name, c) != -1 do
      val p0: Int = text.indexOf(name, c)
      val p1: Int = p0 + name.length()
      highlighter.addHighlight(p0, p1, painter)
      c = p1
  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)

  def countryColor(name: String): Color =
    val hash: Int = name.hashCode

    val r: Int = (hash & 0xff0000) >> 16
    val g: Int = (hash & 0x00ff00) >> 8
    val b: Int = hash & 0x0000ff

    Color(r, g, b)

  def writeToConsole(text: String): Unit =
    this.console.append(text + "\n\n")

  def enableSpeed(x1: Boolean, x2: Boolean, x3: Boolean): Unit =
    this.speed1Button.setEnabled(x1)
    this.speed2Button.setEnabled(x2)
    this.speed3Button.setEnabled(x3)

  def highlightCountryId(id: String): Unit = 
    highlightText(name = id, countryColor(id))