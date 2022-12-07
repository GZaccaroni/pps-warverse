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

trait MenuHelp extends JPanel:
  def addComponentsToPanel(components: Seq[JComponent]): Unit
  def addComponentsToButtonGroup(radioButtons: Seq[JRadioButton]): Unit
  def printInTextField: Unit
  def jsonPrettyFormatted(stringJson: String): String
  def mainFrame: MainFrame

object MenuHelp:
  def apply(mainFrame: MainFrame): MenuHelp = MenuHelpImpl(mainFrame)

  private class MenuHelpImpl(
    override val mainFrame: MainFrame
  ) extends MenuHelp:
    this.requestFocus()
    val startButton = JButton("Start")
    val closeButton = JButton("Exit App")
    val generateJsonButton = JButton("Generate Json")
    val rdbStructure = new JRadioButton("A) Structure");
    val rdbCountry = new JRadioButton("B) Country");
    val rdbUnitKind = new JRadioButton("C) Unit Kind");
    val rdbUnits = new JRadioButton("D) Units");
    val rdbBoundaries = new JRadioButton("E) Boundaries");
    val rdbRelations = new JRadioButton("F) Relations");
    val buttonGroup = new ButtonGroup()
    private val console: JTextArea = JTextArea(25, 25)
    this.console.setMargin(Insets(10, 10, 10, 10))
    this.console.setEditable(false)
    this.console.setLineWrap(true)
    this.console.setWrapStyleWord(true)
    this.addComponentsToButtonGroup(
      Seq(
        rdbStructure,
        rdbCountry,
        rdbUnitKind,
        rdbUnits,
        rdbBoundaries,
        rdbRelations
      )
    )
    this.addComponentsToPanel(
      Seq(
        startButton,
        closeButton,
        rdbStructure,
        rdbCountry,
        rdbUnitKind,
        rdbUnits,
        rdbBoundaries,
        rdbRelations,
        generateJsonButton,
        console
      )
    )
    startButton.addActionListener(_ =>
      GameStateController(mainFrame).setPanel()
    )
    closeButton.addActionListener(_ => System.exit(0))
    generateJsonButton.addActionListener((_ => printInTextField))

    override def jsonPrettyFormatted(stringJson: String): String =
      val mapper = new ObjectMapper
      mapper.enable(SerializationFeature.INDENT_OUTPUT)
      val jsonObject = mapper.readValue(stringJson, classOf[Object])
      mapper.writeValueAsString(jsonObject)

    override def printInTextField: Unit =
      console.setText("")
      if rdbStructure.isSelected then
        console.append(jsonPrettyFormatted(UIConstants.structure))
      if rdbCountry.isSelected then
        console.append(jsonPrettyFormatted(UIConstants.country))
      if rdbUnitKind.isSelected then
        console.append(jsonPrettyFormatted(UIConstants.unit_kinds))
      if rdbUnits.isSelected then
        console.append(jsonPrettyFormatted(UIConstants.unit))
      if rdbBoundaries.isSelected then
        console.append(jsonPrettyFormatted(UIConstants.boundaries))
      if rdbRelations.isSelected then
        console.append(jsonPrettyFormatted(UIConstants.relations))

    override def addComponentsToButtonGroup(
      radioButtons: Seq[JRadioButton]
    ): Unit =
      for radioButton <- radioButtons do this.buttonGroup.add(radioButton)

    override def addComponentsToPanel(components: Seq[JComponent]): Unit =
      for component <- components do this.add(component)
