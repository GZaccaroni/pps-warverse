package it.unibo.warverse.domain.repositories

import it.unibo.warverse.data.data_sources.simulation_config.SimulationConfigDataSource
import it.unibo.warverse.domain.model.common.validation.Validation.ValidationError
import monix.testing.scalatest.MonixTaskTest
import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.matchers.must.Matchers
import org.scalatest.EitherValues.*

import java.io.File

class SimulationConfigRepositoryTest
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
    SimulationConfigRepository()

  test("Parsing should succeed without exceptions") {
    jsonRepository().readSimulationConfig(file).assertNoException
    jsonRepository().readSimulationConfig(invalidFile).assertNoException
  }
  test("Countries should be valid") {
    jsonRepository()
      .readSimulationConfig(file)
      .asserting(simulationConfig =>
        simulationConfig.value.countries.length mustBe 2
        simulationConfig.value.countries.head.id mustBe "Test1"
        simulationConfig.value.countries.last.id mustBe "Test2"
      )
  }
  test("SimulationConfig should not be valid") {
    jsonRepository()
      .readSimulationConfig(invalidFile)
      .asserting(simulationConfig =>
        simulationConfig.left.value mustBe a[List[ValidationError]]
      )
  }
