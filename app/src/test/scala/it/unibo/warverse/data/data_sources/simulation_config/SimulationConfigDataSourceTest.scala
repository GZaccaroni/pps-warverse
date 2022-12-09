package it.unibo.warverse.data.data_sources.simulation_config

import it.unibo.warverse.domain.model.common.validation.Validation.ValidationException
import org.scalatest.funsuite.{AnyFunSuite, AsyncFunSuite}
import org.scalatest.matchers.must.Matchers
import monix.testing.scalatest.MonixTaskTest

import java.io.File

class SimulationConfigDataSourceTest
    extends AsyncFunSuite
    with MonixTaskTest
    with Matchers:
  private val fileURI =
    ClassLoader.getSystemResource("simulation_config_test_sample.json").toURI
  private val file = File(fileURI)
  private val invalidFileURI =
    ClassLoader.getSystemResource("simulation_config_test_error.json").toURI
  private val invalidFile = File(invalidFileURI)

  private def jsonDataSource(file: File) =
    SimulationConfigDataSource(file, SimulationConfigDataSource.Format.Json)

  test("Parsing should succeed without exceptions") {
    jsonDataSource(file).readSimulationConfig().assertNoException
  }
  test("Countries should be valid") {
    jsonDataSource(file)
      .readSimulationConfig()
      .asserting(simulationConfig =>
        simulationConfig.countries.length mustBe 2
        simulationConfig.countries.head.id mustBe "Test1"
        simulationConfig.countries.last.id mustBe "Test2"
      )
  }
  test("SimulationConfig should not be valid") {
    jsonDataSource(invalidFile)
      .readSimulationConfig()
      .assertThrows[ValidationException]
  }
