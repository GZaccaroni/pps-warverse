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
  private val component = WarSimulationComponent()
  private val initialEnv = DomainExample.environment
    .copiedWith(
      countries = DomainExample.environment.countries.map(country =>
        country.addingResources(50)
      ),
      interCountryRelations = DomainExample.environment.interCountryRelations
        .withRelation(
          (DomainExample.countryAId, DomainExample.countryCId),
          RelationStatus.WAR
        )
    )
  test(
    "In new Environment A is defeated by citizen, B and C must obtain his army and citizen"
  ) {
    val envLostCitizen: Environment = initialEnv.replacingCountry(
      initialEnv.countries
        .find(_.id == DomainExample.countryAId)
        .get
        .addingCitizens(-50)
    )
    val envLostForCitizen = component.run(envLostCitizen)
    envLostForCitizen.asserting(newEnv =>
      newEnv.countries.find(_.id == "ID_B").get.resources mustBe 75
      newEnv.countries.find(_.id == "ID_C").get.resources mustBe 75
      newEnv.countries.find(_.id == "ID_B").get.armyUnits.size mustBe 4
      newEnv.countries.find(_.id == "ID_C").get.armyUnits.size mustBe 2
    )
  }

  test(
    "In new Environment A is defeated by resources, B and C must obtain his army and resources"
  ) {
    val envLostResources: Environment = initialEnv.replacingCountry(
      initialEnv.countries.find(_.id == "ID_A").get.addingResources(-50)
    )
    val envLostForResources: Task[Environment] =
      component.run(envLostResources)
    envLostForResources.asserting(newEnv =>
      newEnv.countries.find(_.id == "ID_B").get.citizens mustBe 30
      newEnv.countries.find(_.id == "ID_C").get.citizens mustBe 40
      newEnv.countries.find(_.id == "ID_B").get.armyUnits.size mustBe 4
      newEnv.countries.find(_.id == "ID_C").get.armyUnits.size mustBe 2
    )
  }
