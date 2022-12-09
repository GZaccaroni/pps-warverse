package it.unibo.warverse.data.data_sources.simulation_config

import it.unibo.warverse.data.models.ArmyDtos.{ArmyUnitKind, UnitAttackType}
import it.unibo.warverse.data.models.SimulationConfigDtos.SimulationConfigDto
import it.unibo.warverse.domain.model.common.Geometry
import it.unibo.warverse.domain.model.common.Geometry.{Point2D, Polygon}
import it.unibo.warverse.domain.model.fight.Army
import it.unibo.warverse.data.data_sources.simulation_config.Serializers.*
import it.unibo.warverse.data.models.{ArmyDtos, GeometryDtos}
import it.unibo.warverse.domain.model.SimulationConfig
import it.unibo.warverse.domain.model.common.validation.Validation.ValidationError
import it.unibo.warverse.domain.model.world.Relations.*
import monix.eval.Task
import org.json4s.*
import org.json4s.jackson.JsonMethods.*

import javax.swing.JOptionPane
import scala.util.control.Breaks.*
import java.io.File
import java.text.ParseException

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
