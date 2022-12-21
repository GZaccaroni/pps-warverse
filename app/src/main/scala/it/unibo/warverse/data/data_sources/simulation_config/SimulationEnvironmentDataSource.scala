package it.unibo.warverse.data.data_sources.simulation_config

import it.unibo.warverse.data.models.SimulationEnvironmentDtos.SimulationEnvironmentDto
import it.unibo.warverse.data.data_sources.simulation_config.Serializers.*
import monix.eval.Task
import org.json4s.*
import org.json4s.jackson.JsonMethods.*
import java.io.File

trait SimulationEnvironmentDataSource:
  def readSimulationEnvironment(file: File): Task[SimulationEnvironmentDto]

object SimulationEnvironmentDataSource:

  def apply(): SimulationEnvironmentDataSource =
    JsonSimulationEnvironmentDataSource()

  private class JsonSimulationEnvironmentDataSource()
      extends SimulationEnvironmentDataSource:

    override def readSimulationEnvironment(
      file: File
    ): Task[SimulationEnvironmentDto] =
      Task {
        parse(file)
      }.map(jsonValue =>
        implicit val formats: Formats =
          DefaultFormats + ArmyUnitKindSerializer() + UnitAttackTypeSerializer()
        jsonValue.camelizeKeys.extract[SimulationEnvironmentDto]
      )
