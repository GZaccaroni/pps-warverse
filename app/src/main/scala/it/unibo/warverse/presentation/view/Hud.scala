package it.unibo.warverse.presentation.view
import it.unibo.warverse.domain.model.world.World
import it.unibo.warverse.presentation.inputs.GameMouseMotion

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
import java.awt.Graphics
import javax.swing.BorderFactory
import it.unibo.warverse.presentation.common.UIConstants

import javax.swing.JTextPane
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

import scala.io.Source
import javax.swing.JOptionPane
import it.unibo.warverse.domain.model.world.Relations
import it.unibo.warverse.domain.model.world.Relations.InterstateRelations

class Hud extends GameMouseMotion:
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
  var countries: List[World.Country] = _
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
    toggleSimulationButton.getText match
      case "Start" =>
        controller.startClicked(); toggleSimulationButton.setText("Pause")
      case "Pause" =>
        controller.pauseClicked(); toggleSimulationButton.setText("Resume")
      case "Resume" =>
        controller.resumeClicked(); toggleSimulationButton.setText("Pause")
  )
  stopButton.addActionListener(_ => controller.stopClicked())

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

  def updateConsole(relations: InterstateRelations): Unit =
    countries.foreach(country =>
      console.append(
        country.name + " start with " + country.citizens + " Citizen, " + country.armyUnits.length + " Army units and " + String
          .format("%.02f", country.resources) + " Resources\n\n"
      )
    )

    countries.foreach(country =>
      relations
        .getAllies(country.id)
        .foreach(allyId =>
          val ally = countries.find(_.id == allyId).get;
          console.append(country.name + " is allied with " + ally.name + "\n\n")
        )
      relations
        .getEnemies(country.id)
        .foreach(enemyId =>
          val enemy = countries.find(_.id == enemyId).get;
          console.append(
            country.name + " is in war with " + enemy.name + "\n\n"
          )
        )
      highlightText(
        console.getText,
        country.name,
        Color.decode(super.getCountryColor(country.name))
      )
    )

    highlightText(console.getText, "allied", Color(0, 153, 0))
    highlightText(console.getText, "war", Color.RED)

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

  def addJComponents(box: Box, list: List[JComponent]): Unit =
    list.foreach(component => box.add(component))

  def getExtensionByStringHandling(filename: String): Boolean =
    filename.split("\\.").last == "json"

  def uploadJson(): Unit =
    fileChooser.showOpenDialog(this)
    val file = fileChooser.getSelectedFile
    if getExtensionByStringHandling(file.getName) then

      val jsonConfigParser =
        SimulationConfigDataSource(file, SimulationConfigDataSource.Format.Json)
      try
        val simulationConfig = jsonConfigParser.simulationConfig
        this.controller.setAllCountries(simulationConfig.countries)
        this.controller.setInterstateRelations(
          simulationConfig.interstateRelations
        )
        super.setCountries(simulationConfig.countries)
        this.countries = simulationConfig.countries
        updateConsole(this.controller.getRelationship)
        JOptionPane.showMessageDialog(
          null,
          "Configuration uploaded successfully."
        )
      catch
        case _: NullPointerException =>
          println("Configuration File have some errors.")

  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)
