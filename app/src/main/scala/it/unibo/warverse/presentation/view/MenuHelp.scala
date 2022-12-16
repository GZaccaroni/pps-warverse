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
import it.unibo.warverse.data.samples.EncodedSamples

import java.net.URL
import java.awt.Graphics
import java.awt.Toolkit
import javax.swing.JLabel
import java.awt.Font

trait MenuHelp extends JPanel:
  def addComponentsToPanel(components: Seq[JComponent]): Unit
  def addComponentsToButtonGroup(radioButtons: Seq[JRadioButton]): Unit
  def mainFrame: MainFrame

object MenuHelp:
  def apply(mainFrame: MainFrame): MenuHelp = MenuHelpImpl(mainFrame)

  private class MenuHelpImpl(
    override val mainFrame: MainFrame
  ) extends MenuHelp:
    this.requestFocus()
    this.setLayout(null)
    private val encodedSamples = EncodedSamples()
    private val startButton = JButton("Start")
    private val closeButton = JButton("Exit App")
    private val generateJsonButton = JButton("Get Json Sample")
    private val rdbStructure = JRadioButton("A) Structure")
    private val rdbCountry = JRadioButton("B) Country")
    private val rdbUnitKind = JRadioButton("C) Unit Kind")
    private val rdbUnits = JRadioButton("D) Units")
    private val rdbBoundaries = JRadioButton("E) Boundaries")
    private val rdbRelations = JRadioButton("F) Relations")
    private val buttonGroup = ButtonGroup()
    private val console: JTextArea = JTextArea(25, 25)
    private val text: JLabel = JLabel(UIConstants.helpDescription)
    text.setFont(Font("Serif", Font.PLAIN, 20))
    text.setBounds(20, 120, 900, 200)
    rdbStructure.setBounds(920, 60, 120, 20)
    rdbCountry.setBounds(1020, 60, 120, 20)
    rdbUnitKind.setBounds(1130, 60, 120, 20)
    rdbUnits.setBounds(920, 80, 100, 20)
    rdbBoundaries.setBounds(1020, 80, 120, 20)
    rdbRelations.setBounds(1130, 80, 120, 20)
    generateJsonButton.setBounds(1240, 55, 120, 50)
    console.setBounds(920, 110, 400, 300)
    startButton.setBounds(650, 300, 100, 50)
    closeButton.setBounds(750, 300, 100, 50)

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
        text,
        rdbStructure,
        rdbCountry,
        rdbUnitKind,
        rdbUnits,
        rdbBoundaries,
        rdbRelations,
        generateJsonButton,
        console,
        startButton,
        closeButton
      )
    )
    startButton.addActionListener(_ =>
      GameStateController(mainFrame).setPanel()
    )
    closeButton.addActionListener(_ => System.exit(0))
    generateJsonButton.addActionListener(_ => updateTextField())

    private def updateTextField(): Unit =
      console.setText("")
      if rdbStructure.isSelected then console.append(encodedSamples.complete)
      if rdbCountry.isSelected then console.append(encodedSamples.country)
      if rdbUnitKind.isSelected then console.append(encodedSamples.armyUnitKind)
      if rdbUnits.isSelected then console.append(encodedSamples.armyUnit)
      if rdbBoundaries.isSelected then
        console.append(encodedSamples.countryBoundaries)
      if rdbRelations.isSelected then
        console.append(encodedSamples.countryRelations)

    override def addComponentsToButtonGroup(
      radioButtons: Seq[JRadioButton]
    ): Unit =
      for radioButton <- radioButtons do this.buttonGroup.add(radioButton)

    override def addComponentsToPanel(components: Seq[JComponent]): Unit =
      for component <- components do this.add(component)

    override def paintComponent(g: Graphics): Unit =
      super.paintComponent(g)
      val backgroundImage = Option(UIConstants.Resources.HelpMenuBackground.url)
        .map(Toolkit.getDefaultToolkit.getImage(_))
      backgroundImage.foreach(
        g.drawImage(_, 0, 0, this.getSize().width, this.getSize().height, this)
      )
