package it.unibo.warverse.presentation.view

import it.unibo.warverse.presentation.common.UIConstants
import javax.swing.JPanel
import javax.swing.JButton
import it.unibo.warverse.presentation.controllers.GameStateController
import javax.swing.JRadioButton
import javax.swing.ButtonGroup
import javax.swing.JComponent
import javax.swing.JTextArea
import java.awt.Insets
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

class MenuHelp(mainFrame: MainFrame) extends JPanel:
  this.requestFocus()
  val startButton = JButton("Start")
  val closeButton = JButton("Exit App")
  val generateJsonButton = JButton("Generate Json")
  val r1 = new JRadioButton("A) Structure");
  val r2 = new JRadioButton("B) Country");
  val r3 = new JRadioButton("C) Unit Kind");
  val r4 = new JRadioButton("D) Units");
  val r5 = new JRadioButton("E) Boundaries");
  val r6 = new JRadioButton("F) Relations");
  val bg = new ButtonGroup()
  private val console: JTextArea = JTextArea(25, 25)
  this.console.setMargin(Insets(10, 10, 10, 10))
  this.console.setEditable(false)
  this.console.setLineWrap(true)
  this.console.setWrapStyleWord(true)
  bg.add(r1)
  bg.add(r2)
  bg.add(r3)
  bg.add(r4)
  bg.add(r5)
  bg.add(r6)
  this.addComponentsToPanel(
    List(
      startButton,
      closeButton,
      r1,
      r2,
      r3,
      r4,
      r5,
      r6,
      generateJsonButton,
      console
    )
  )
  startButton.addActionListener(_ => GameStateController(mainFrame).setPanel())
  closeButton.addActionListener(_ => System.exit(0))
  generateJsonButton.addActionListener((_ => printInTextField))

  def jsonPrettyFormatted(stringJson: String): String =
    val mapper = new ObjectMapper
    mapper.enable(SerializationFeature.INDENT_OUTPUT)
    val jsonObject = mapper.readValue(stringJson, classOf[Object])
    mapper.writeValueAsString(jsonObject)

  def printInTextField: Unit =
    console.setText("")
    if r1.isSelected then
      console.append(jsonPrettyFormatted(UIConstants.structure))
    if r2.isSelected then
      console.append(jsonPrettyFormatted(UIConstants.country))
    if r3.isSelected then
      console.append(jsonPrettyFormatted(UIConstants.unit_kinds))
    if r4.isSelected then console.append(jsonPrettyFormatted(UIConstants.unit))
    if r5.isSelected then
      console.append(jsonPrettyFormatted(UIConstants.boundaries))
    if r6.isSelected then
      console.append(jsonPrettyFormatted(UIConstants.relations))

  def addComponentsToPanel(components: List[JComponent]): Unit =
    for component <- components do this.add(component)
