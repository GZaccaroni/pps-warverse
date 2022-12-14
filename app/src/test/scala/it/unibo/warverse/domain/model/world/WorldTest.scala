package it.unibo.warverse.domain.model.world

import it.unibo.warverse.domain.model.common.DomainExample
import it.unibo.warverse.domain.model.common.DomainExample.Army.{
  AreaArmyUnits,
  PrecisionArmyUnits
}
import it.unibo.warverse.domain.model.common.DomainExample.{
  countryA,
  countryBTerritory
}
import org.scalatest.Suites
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

object WorldTest:
  class CountryTest extends AnyFunSuite with Matchers:
    test(
      "Result of adding resources to a country must be a new country with resources updated"
    ) {
      val resources = 1000
      countryA.addingResources(resources) must not be countryA
      countryA
        .addingResources(resources)
        .resources mustBe countryA.resources + resources
    }

    test(
      "Result of adding citizen to a country must be a new country with citizen updated"
    ) {
      val citizens = 100
      countryA.addingCitizens(citizens) must not be countryA
      countryA
        .addingCitizens(citizens)
        .citizens mustBe countryA.citizens + citizens
    }

    test(
      "Result of adding army units to a country must be a new country with army units updated"
    ) {
      val armyUnits =
        Seq(AreaArmyUnits.successfulUnit, PrecisionArmyUnits.failingUnit)
      countryA.addingArmyUnits(armyUnits) must not be countryA
      countryA
        .addingArmyUnits(armyUnits)
        .armyUnits must contain theSameElementsAs (countryA.armyUnits ++ armyUnits)
    }

    test(
      "Result of adding territory to a country must be a new country with territory updated"
    ) {
      countryA.addingTerritory(countryBTerritory) must not be countryA
      countryA
        .addingTerritory(countryBTerritory)
        .boundaries
        .polygons must contain theSameElementsAs countryA.boundaries.polygons ++ countryBTerritory.polygons
    }

class WorldTest extends Suites(WorldTest.CountryTest())
