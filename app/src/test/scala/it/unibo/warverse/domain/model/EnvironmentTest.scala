package it.unibo.warverse.domain.model

import it.unibo.warverse.domain.model.common.DomainExample
import it.unibo.warverse.domain.model.common.DomainExample.countryA
import it.unibo.warverse.domain.model.world.Relations.InterCountryRelations
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

class EnvironmentTest extends AnyFunSuite with Matchers:
  val environment: Environment = DomainExample.environment

  test("At the beginning the day must start at 0") {
    environment.day mustBe 0
  }

  test("Environment must be copied with new parameters correctly") {
    environment.copiedWith(day = 1).day mustBe 1
    environment.copiedWith(day = 1).countries mustBe environment.countries
    environment
      .copiedWith(interCountryRelations = InterCountryRelations(Set.empty))
      .interCountryRelations mustBe InterCountryRelations(Set.empty)
    environment
      .copiedWith(
        Seq.empty,
        InterCountryRelations(Set.empty)
      )
      .countries mustBe empty
  }

  test("A country should be replaced by the given one") {
    val updatedCountry = countryA.addingCitizens(100)
    environment
      .replacingCountry(updatedCountry)
      .countries must not contain countryA
    environment
      .replacingCountry(updatedCountry)
      .countries must contain(updatedCountry)
  }
