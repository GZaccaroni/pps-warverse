package it.unibo.warverse.domain.repositories

import it.unibo.warverse.data.data_sources.simulation_config.SimulationConfigDataSource
import it.unibo.warverse.domain.model.SimulationConfig
import it.unibo.warverse.domain.model.common.validation.Validation.ValidationError
import it.unibo.warverse.data.repositories.SimulationConfigRepositoryImpl
import monix.eval.Task

import java.io.File

trait SimulationConfigRepository:
  def readSimulationConfig(
    file: File
  ): Task[Either[List[ValidationError], SimulationConfig]]

object SimulationConfigRepository:
  def apply(): SimulationConfigRepository = SimulationConfigRepositoryImpl(
    SimulationConfigDataSource()
  )
