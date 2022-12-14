package it.unibo.warverse.domain.engine.simulation.components

import it.unibo.warverse.domain.model.Environment
import org.scalatest.matchers.must.Matchers
import it.unibo.warverse.domain.model.common.DomainExample
import it.unibo.warverse.domain.model.world.Relations.*
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import org.scalatest.funsuite.AsyncFunSuite
import monix.testing.scalatest.MonixTaskTest

class WarSimulationComponentTest
    extends AsyncFunSuite
    with MonixTaskTest
    with Matchers:
  var environment = DomainExample.environment
    .copiedWith(
      DomainExample.environment.countries.map(country =>
        country.addingResources(50)
      )
    )

  RelationsSimulationComponent()
    .run(environment)
    .foreach(env => this.environment = env)

  test("A and C must be in war after relations update") {
    environment.interCountryRelations
      .relationStatus("ID_A", "ID_C")
      .head mustBe RelationStatus.WAR
  }

  val envLostCitizen: Environment = environment.replacingCountry(
    environment.countries.find(_.id == "ID_A").get.addingCitizens(-50)
  )

  test("Citizen of A must be zero") {
    envLostCitizen.countries.find(_.id == "ID_A").get.citizens mustBe 0
  }

  val envLostForCitizen: Task[Environment] =
    WarSimulationComponent().run(envLostCitizen)
  test(
    "In new Environment A is defeated by citizen, B and C must obtain his army and citizen"
  ) {
    envLostForCitizen.asserting(newEnv =>
      newEnv.countries.find(_.id == "ID_B").get.resources mustBe 75
      newEnv.countries.find(_.id == "ID_C").get.resources mustBe 75
      newEnv.countries.find(_.id == "ID_B").get.armyUnits.size mustBe 4
      newEnv.countries.find(_.id == "ID_C").get.armyUnits.size mustBe 2
    )
  }
  val envLostResources: Environment = environment.replacingCountry(
    environment.countries.find(_.id == "ID_A").get.addingResources(-50)
  )
  val envLostForResources: Task[Environment] =
    WarSimulationComponent().run(envLostResources)

  test(
    "In new Environment A is defeated by resources, B and C must obtain his army and resources"
  ) {
    envLostForResources.asserting(newEnv =>
      newEnv.countries.find(_.id == "ID_B").get.citizens mustBe 30
      newEnv.countries.find(_.id == "ID_C").get.citizens mustBe 40
      newEnv.countries.find(_.id == "ID_B").get.armyUnits.size mustBe 4
      newEnv.countries.find(_.id == "ID_C").get.armyUnits.size mustBe 2
    )
  }
