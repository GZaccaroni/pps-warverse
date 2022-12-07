package it.unibo.warverse.presentation.view
import it.unibo.warverse.domain.model.world.World
import it.unibo.warverse.presentation.inputs.GameMouseMotion
import monix.execution.Scheduler.Implicits.global

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

  speed1Button.addActionListener(_ => console.append("Speed X1\n"))
  speed2Button.addActionListener(_ => console.append("Speed X2\n"))
  speed3Button.addActionListener(_ => console.append("Speed X3\n"))
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
      val simulationConfigTask = jsonConfigParser.readSimulationConfig()
      simulationConfigTask.runAsync {
        case Right(simulationConfig) =>
          displayInitialSimulationConfig(simulationConfig)
          controller.simulationConfig = Some(simulationConfig)
          JOptionPane.showMessageDialog(
            null,
            "Configuration uploaded successfully."
          )
        case Left(error) =>
          println(s"Configuration File have some errors. ${error}")
      }

  private def displayInitialSimulationConfig(
    simulationConfig: SimulationConfig
  ): Unit =
    console.setText("")
    simulationConfig.countries.foreach(country =>
      console.append(
        country.name + " starts with " + country.citizens + " citizen, " + country.armyUnits.length + " army units and " + String
          .format("%.02f", country.resources) + " resources.\n\n"
      )
    )

    simulationConfig.countries.foreach(country =>
      simulationConfig.interstateRelations
        .countryAllies(country.id)
        .foreach(allyId =>
          val ally = simulationConfig.countries.find(_.id == allyId).get;
          console.append(country.name + " is allied with " + ally.name + "\n\n")
        )
      simulationConfig.interstateRelations
        .countryEnemies(country.id)
        .foreach(enemyId =>
          val enemy = simulationConfig.countries.find(_.id == enemyId).get;
          console.append(
            country.name + " is in war with " + enemy.name + "\n\n"
          )
        )
    )
    simulationConfig.countries.foreach(country =>
      highlightText(
        console.getText,
        country.name,
        countryColor(country.id)
      )
    )
    highlightText(console.getText, "allied", Color(0, 153, 0))
    highlightText(console.getText, "war", Color.RED)

  private def highlightText(text: String, name: String, color: Color): Unit =
    var c: Integer = 0
    val painter: HighlightPainter =
      DefaultHighlighter.DefaultHighlightPainter(color)
    while text.indexOf(name, c) != -1 do
      val p0: Integer = text.indexOf(name, c)
      val p1: Integer = p0 + name.length()
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
