package it.unibo.warverse

import it.unibo.warverse.data.data_sources.json.SimulationConfigDataSource
import it.unibo.warverse.ui.view.MainFrame

import java.awt.{BorderLayout, Dimension}
import java.io.File
import javax.swing.{JFrame, JPanel, SwingUtilities}
import scala.io.Source

object LauncherJson extends App:
  val jsonFile = File(
    "test_v2.json"
  )

  val jsonConfigParser: SimulationConfigDataSource = SimulationConfigDataSource(
    jsonFile
  )

  println(jsonConfigParser.simulationConfig)
