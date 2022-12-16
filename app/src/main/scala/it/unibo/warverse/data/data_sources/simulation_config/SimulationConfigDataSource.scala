package it.unibo.warverse.data.data_sources.simulation_config

import it.unibo.warverse.data.models.SimulationConfigDtos.SimulationConfigDto
import it.unibo.warverse.data.data_sources.simulation_config.Serializers.*
import monix.eval.Task
import org.json4s.*
import org.json4s.jackson.JsonMethods.*
import java.io.File

trait SimulationConfigDataSource:
  def readSimulationConfig(file: File): Task[SimulationConfigDto]

object SimulationConfigDataSource:

  def apply(): SimulationConfigDataSource =
    JsonSimulationConfigDataSource()

  private class JsonSimulationConfigDataSource()
      extends SimulationConfigDataSource:

    override def readSimulationConfig(
      file: File
    ): Task[SimulationConfigDto] =
      Task {
        parse(file)
      }.map(jsonValue =>
        implicit val formats: Formats =
          DefaultFormats + ArmyUnitKindSerializer() + UnitAttackTypeSerializer()
        jsonValue.camelizeKeys.extract[SimulationConfigDto]
      )
