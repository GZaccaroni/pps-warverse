package it.unibo.warverse.domain.model.fight

import it.unibo.warverse.domain.model.common.DomainExample.{
  countryA,
  countryB,
  distantSoldier,
  environment,
  failingSoldier,
  successfullySoldier
}
import it.unibo.warverse.domain.model.common.Geometry.Point2D
import it.unibo.warverse.domain.model.fight.Army.AreaArmyUnit
import it.unibo.warverse.domain.model.fight.SimulationEvent.AreaAttackEvent
import org.scalatest.Suites
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

object ArmyTest:
  class AreaArmyUnitTest extends AnyFunSuite with Matchers:

    val countryAStrategy: AttackStrategy.AttackStrategy2D =
      AttackStrategy.attackStrategy2D(environment, countryA.id)

    test("A unit with no target in range should attack nobody") {
      distantSoldier.attack(countryAStrategy) mustBe Seq.empty
    }

    test("An attack of a units with 100% chance of hit should be successful") {
      successfullySoldier.attack(countryAStrategy) mustBe Seq(
        AreaAttackEvent(
          countryB.boundaries.center,
          successfullySoldier.areaOfImpact
        )
      )
    }

    test("An attack of a units with 0% chance of hit should fail") {
      failingSoldier.attack(countryAStrategy) mustBe Seq.empty
    }

class ArmyTest
    extends Suites(
      ArmyTest.AreaArmyUnitTest()
    )
