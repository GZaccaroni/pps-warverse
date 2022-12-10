package it.unibo.warverse.domain.model

import it.unibo.warverse.domain.model.common.DomainExample
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

object EnvironmentTest:
  class EnvironmentTest extends AnyFunSuite with Matchers:
    val environment = DomainExample.environment

    test("At the beginning the day must start at 0") {
      environment.day mustBe (0)
    }

    test("When a day passed the environment must be updated") {
      environment.copiedWith(day = 1)
      environment.day mustBe (1)
    }

    test("Army size must increase after adding new units") {
      val country = environment.countries.find(_.id == "ID_A").get
      val countryArmy = country.armyUnits
      environment.replacingCountry(country.addingArmyUnits(countryArmy.concat(countryArmy)))
      environment.countries.find(_.id == "ID_A").get.armyUnits.size mustBe(countryArmy.size*2)
    }

    test("Size of the initial states must be 3") {
      environment.countries.size mustBe(3)
    }
