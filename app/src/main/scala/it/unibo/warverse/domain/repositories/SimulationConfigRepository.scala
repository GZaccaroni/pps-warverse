package it.unibo.warverse.domain.repositories

import it.unibo.warverse.data.data_sources.simulation_config.SimulationConfigDataSource
import it.unibo.warverse.domain.model.SimulationConfig
import it.unibo.warverse.domain.model.common.validation.Validation.ValidationError
import it.unibo.warverse.data.repositories.SimulationConfigRepositoryImpl
import monix.eval.Task

import java.io.File

/** Reads simulation config from a specific source
  */
trait SimulationConfigRepository:
  /** Reads simulation config from a specific source
    * @param file
    *   the source to read from
    * @return
    *   the simulation config or a list of validation errors if the reading
    *   fails
    */
  def readSimulationConfig(
    file: File
  ): Task[Either[List[ValidationError], SimulationConfig]]

object SimulationConfigRepository:
  def apply(): SimulationConfigRepository = SimulationConfigRepositoryImpl(
    SimulationConfigDataSource()
  )
