package it.unibo.warverse.data.data_sources.simulation_config

import it.unibo.warverse.domain.model.common.Validation.ValidationException
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

import java.io.File

class SimulationConfigDataSourceTest extends AnyFunSuite with Matchers:
  private val fileURI =
    ClassLoader.getSystemResource("simulation_config_test_sample.json").toURI
  private val file = File(fileURI)
  private val invalidFileURI =
    ClassLoader.getSystemResource("simulation_config_test_error.json").toURI
  private val invalidFile = File(invalidFileURI)

  private def jsonDataSource(file: File) =
    SimulationConfigDataSource(file, SimulationConfigDataSource.Format.Json)

  test("Parsing should succeed without exceptions") {
    noException must be thrownBy jsonDataSource(file).simulationConfig
  }
  test("Countries should be valid") {
    val simulationConfig = jsonDataSource(file).simulationConfig
    simulationConfig.countries.length mustBe 2
    simulationConfig.countries.head.id mustBe "Test1"
    simulationConfig.countries.last.id mustBe "Test2"
  }
  test("SimulationConfig should not be valid") {
    an[ValidationException] mustBe thrownBy(
      jsonDataSource(invalidFile).simulationConfig
    )
  }
