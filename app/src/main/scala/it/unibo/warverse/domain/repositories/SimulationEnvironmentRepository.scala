package it.unibo.warverse.domain.repositories

import it.unibo.warverse.data.data_sources.simulation_config.SimulationEnvironmentDataSource
import it.unibo.warverse.domain.model.common.validation.Validation.ValidationError
import it.unibo.warverse.data.repositories.SimulationEnvironmentRepositoryImpl
import it.unibo.warverse.domain.model.Environment
import monix.eval.Task

import java.io.File

/** Reads simulation environment from a specific source
  */
trait SimulationEnvironmentRepository:
  /** Reads simulation environment from a specific source
    * @param file
    *   the source to read from
    * @return
    *   the simulation environment or a list of validation errors if the reading
    *   fails
    */
  def readSimulationEnvironment(
    file: File
  ): Task[Either[List[ValidationError], Environment]]

object SimulationEnvironmentRepository:
  def apply(): SimulationEnvironmentRepository =
    SimulationEnvironmentRepositoryImpl(
      SimulationEnvironmentDataSource()
    )
