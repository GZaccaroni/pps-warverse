package it.unibo.warverse.presentation.view

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.presentation.controllers.GameStateController
import it.unibo.warverse.presentation.common.toColor
import it.unibo.warverse.domain.repositories.SimulationEnvironmentRepository

import java.io.File
import java.awt.{Color, Dimension, Graphics, Insets}
import javax.swing.text.{DefaultCaret, DefaultHighlighter, Highlighter}
import javax.swing.text.Highlighter.HighlightPainter
import javax.swing.{
  Box,
  JButton,
  JComponent,
  JFileChooser,
  JOptionPane,
  JPanel,
  JRadioButton,
  JScrollPane,
  JTextArea
}
import monix.execution.Scheduler.Implicits.global

import javax.swing.filechooser.FileNameExtensionFilter

trait Hud extends JPanel:
  def addJComponents(box: Box, list: Seq[JComponent]): Unit
  def uploadJson(): Unit
  def displayInitialSimulationEnvironment(
    environment: Environment
  ): Unit
  def highlightText(
    text: String,
    name: String,
    color: Color
  ): Unit
  def writeToConsole(text: String): Unit
  def enableSpeed(x1: Boolean, x2: Boolean, x3: Boolean): Unit
  def highlightCountryId(id: String): Unit
  def controller: GameStateController

object Hud:
  def apply(controller: GameStateController): Hud = HudImpl(controller)

  private class HudImpl(override val controller: GameStateController)
      extends Hud:
    this.setPreferredSize(Dimension(350, 20))
    private val uploadConfig = JButton("Upload Configuration")
    private val fileChooser = JFileChooser()
    private val toggleSimulationButton = JButton("Start")
    private val stopButton = JButton("Stop")
    private val speed1Button = JRadioButton("X1")
    private val speed2Button = JRadioButton("X2")
    private val speed3Button = JRadioButton("X3")
    private val verticalContainer = Box.createVerticalBox()
    private val firstButtonsRow = Box.createHorizontalBox()
    private val secondButtonsRow = Box.createHorizontalBox()
    private val console: JTextArea = JTextArea(25, 25)
    private val caret: DefaultCaret =
      console.getCaret.asInstanceOf[DefaultCaret]
    private val highlighter: Highlighter = console.getHighlighter
    private val gameStatus: JScrollPane = JScrollPane(console)

    gameStatus.setVerticalScrollBarPolicy(22)
    this.add(uploadConfig)
    this.add(gameStatus)

    toggleSimulationButton.setEnabled(false)
    toggleSimulationButton.setForeground(Color.BLUE)
    stopButton.setForeground(Color.RED)
    stopButton.setEnabled(false)
    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE)
    console.setBackground(Color.BLACK)
    console.setForeground(Color.WHITE)
    console.setMargin(Insets(10, 10, 10, 10))
    console.setEditable(false)
    console.setLineWrap(true)
    console.setWrapStyleWord(true)

    fileChooser.setAcceptAllFileFilterUsed(false)
    fileChooser.addChoosableFileFilter(
      FileNameExtensionFilter("Json Document", "json")
    )
    fileChooser.setCurrentDirectory(
      File(
        s"${System.getProperty("user.home")}${System.getProperty("file.separator")}Desktop"
      )
    )
    uploadConfig.addActionListener(_ => uploadJson())

    toggleSimulationButton.addActionListener(_ =>
      (toggleSimulationButton.getText, controller.simulationEnvironment) match
        case ("Start", Some(_)) =>
          uploadConfig.setEnabled(false)
          controller.onStartClicked()
          stopButton.setEnabled(true)
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

    enableSpeed(true, false, false)
    speed1Button.addActionListener(_ =>
      writeToConsole("Speed set to X1")
      controller.changeSpeed(5)
      enableSpeed(true, false, false)
    )
    speed2Button.addActionListener(_ =>
      writeToConsole("Speed set to X2")
      controller.changeSpeed(15)
      enableSpeed(false, true, false)
    )
    speed3Button.addActionListener(_ =>
      writeToConsole("Speed set to X3")
      controller.changeSpeed(30)
      enableSpeed(false, false, true)
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

    override def addJComponents(box: Box, list: Seq[JComponent]): Unit =
      list.foreach(component => box.add(component))

    override def uploadJson(): Unit =
      fileChooser.showOpenDialog(this)
      val fileOption = Option(fileChooser.getSelectedFile)
      for file <- fileOption do
        val jsonConfigParser =
          SimulationEnvironmentRepository()
        val simulationEnvTask =
          jsonConfigParser.readSimulationEnvironment(file)
        simulationEnvTask.runAsync {
          case Right(Right(simulationEnv)) =>
            displayInitialSimulationEnvironment(simulationEnv)
            controller.simulationEnvironment = Some(simulationEnv)
            JOptionPane.showMessageDialog(
              null,
              "Configuration uploaded successfully."
            )
            toggleSimulationButton.setEnabled(true)
          case Right(Left(errors)) =>
            JOptionPane.showMessageDialog(
              null,
              s"Validation failed: ${errors.map(_.toString).mkString(",")}."
            )
          case Left(error) =>
            JOptionPane.showMessageDialog(
              null,
              s"Couldn't read configuration file: $error"
            )
        }

    override def displayInitialSimulationEnvironment(
      environment: Environment
    ): Unit =
      console.setText("")
      environment.countries.foreach(country =>
        writeToConsole(
          s"${country.name} starts with ${country.citizens} citizen, ${country.armyUnits.length} army units and ${String
              .format("%.02f", country.resources)} resources."
        )
      )

      environment.countries.foreach(country =>
        environment.interCountryRelations
          .countryAllies(country.id)
          .foreach(allyId =>
            val ally = environment.countries.find(_.id == allyId).get
            writeToConsole(s"${country.name} is allied with ${ally.name}")
          )
        environment.interCountryRelations
          .countryEnemies(country.id)
          .foreach(enemyId =>
            val enemy = environment.countries.find(_.id == enemyId).get
            writeToConsole(
              s"${country.name} is in war with ${enemy.name}"
            )
          )
      )
      environment.countries.foreach(country =>
        highlightText(
          name = country.name,
          color = country.id.toColor
        )
      )
      highlightText(name = "allied", Color(0, 153, 0))
      highlightText(name = "war", Color.RED)
      writeToConsole("Default speed is set to X1")

    override def highlightText(
      text: String = console.getText,
      name: String,
      color: Color
    ): Unit =
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

    override def writeToConsole(text: String): Unit =
      this.console.append(s"$text\n\n")

    override def enableSpeed(x1: Boolean, x2: Boolean, x3: Boolean): Unit =
      this.speed1Button.setSelected(x1)
      this.speed2Button.setSelected(x2)
      this.speed3Button.setSelected(x3)

    def highlightCountryId(id: String): Unit =
      highlightText(name = id, id.toColor)
