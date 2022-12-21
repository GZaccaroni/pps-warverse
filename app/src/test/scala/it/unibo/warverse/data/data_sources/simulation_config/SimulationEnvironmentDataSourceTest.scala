package it.unibo.warverse.data.data_sources.simulation_config

import it.unibo.warverse.domain.model.common.validation.Validation.ValidationError
import org.scalatest.funsuite.{AnyFunSuite, AsyncFunSuite}
import org.scalatest.matchers.must.Matchers
import monix.testing.scalatest.MonixTaskTest
import org.scalatest.EitherValues.*

import java.io.File

class SimulationEnvironmentDataSourceTest
    extends AsyncFunSuite
    with MonixTaskTest
    with Matchers:
  private val fileURI =
    ClassLoader.getSystemResource("simulation_config_test_sample.json").toURI
  private val file = File(fileURI)
  private val invalidFileURI =
    ClassLoader.getSystemResource("simulation_config_test_error.json").toURI
  private val invalidFile = File(invalidFileURI)

  private def jsonDataSource() =
    SimulationEnvironmentDataSource()

  test("Parsing should succeed without exceptions") {
    jsonDataSource().readSimulationEnvironment(file).assertNoException
    jsonDataSource().readSimulationEnvironment(invalidFile).assertNoException
  }
  test("Countries should be valid") {
    jsonDataSource()
      .readSimulationEnvironment(file)
      .asserting(simulationEnvironment =>
        simulationEnvironment.countries.length mustBe 2
        simulationEnvironment.countries.head.id mustBe "Test1"
        simulationEnvironment.countries.last.id mustBe "Test2"
      )
  }
