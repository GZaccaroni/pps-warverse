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
import java.io.FileInputStream
import scala.io.Source
import javax.swing.JFileChooser
import java.io.File
import it.unibo.warverse.model.world.World.Country
import org.json4s.*
import org.json4s.jackson.JsonMethods.*
import it.unibo.warverse.model.world.World.*
import it.unibo.warverse.model.fight.Army.*
import it.unibo.warverse.model.common.Geometry
import java.awt.geom.Point2D
import it.unibo.warverse.model.fight.Army
import it.unibo.warverse.ui.common.JsonConfigParser

class Hud extends GameMouseMotion:
  this.setPreferredSize(new Dimension(350, 20))
  val uploadConfig = new JButton("Upload Configuration")
  val fileChooser = new JFileChooser()
  fileChooser.setCurrentDirectory(
    new File(
      System.getProperty("user.home") + System.getProperty(
        "file.separator"
      ) + "Desktop"
    )
  )
  uploadConfig.addActionListener(e => uploadJson())
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
  private val gameStatus: JScrollPane = new JScrollPane(console)
  gameStatus.setVerticalScrollBarPolicy(22)

  this.add(uploadConfig)
  this.add(gameStatus)

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

  def uploadJson(): Unit =
    fileChooser.showOpenDialog(this)
    val file = fileChooser.getSelectedFile()
    val jsonFile = scala.io.Source
      .fromFile(file)
      .mkString
    val jsonConfigParser: JsonConfigParser = JsonConfigParser(jsonFile)
    val resJson = jsonConfigParser.getConfig()
    println(super.getCountries())
    super.setCountries(resJson)

  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)
