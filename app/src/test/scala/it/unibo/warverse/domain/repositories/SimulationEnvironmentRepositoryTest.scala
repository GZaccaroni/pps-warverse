package it.unibo.warverse.domain.repositories

import it.unibo.warverse.data.data_sources.simulation_config.SimulationEnvironmentDataSource
import it.unibo.warverse.domain.model.common.validation.Validation.ValidationError
import monix.testing.scalatest.MonixTaskTest
import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.matchers.must.Matchers
import org.scalatest.EitherValues.*

import java.io.File

class SimulationEnvironmentRepositoryTest
    extends AsyncFunSuite
    with MonixTaskTest
    with Matchers:
  private val fileURI =
    ClassLoader.getSystemResource("simulation_config_test_sample.json").toURI
  private val file = File(fileURI)
  private val invalidFileURI =
    ClassLoader.getSystemResource("simulation_config_test_error.json").toURI
  private val invalidFile = File(invalidFileURI)

  private def jsonRepository() =
    SimulationEnvironmentRepository()

  test("Parsing should succeed without exceptions") {
    jsonRepository().readSimulationEnvironment(file).assertNoException
    jsonRepository().readSimulationEnvironment(invalidFile).assertNoException
  }
  test("Countries should be valid") {
    jsonRepository()
      .readSimulationEnvironment(file)
      .asserting(simulationEnv =>
        simulationEnv.value.countries.length mustBe 2
        simulationEnv.value.countries.head.id mustBe "Test1"
        simulationEnv.value.countries.last.id mustBe "Test2"
      )
  }
  test("SimulationEnvironment should not be valid") {
    jsonRepository()
      .readSimulationEnvironment(invalidFile)
      .asserting(simulationEnv =>
        simulationEnv.left.value mustBe a[List[ValidationError]]
      )
  }
