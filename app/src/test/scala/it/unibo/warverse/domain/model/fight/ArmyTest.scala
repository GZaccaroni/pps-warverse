package it.unibo.warverse.domain.model.fight

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.common.DomainExample
import it.unibo.warverse.domain.model.common.DomainExample.*
import it.unibo.warverse.domain.model.common.Geometry.Point2D
import it.unibo.warverse.domain.model.fight.Army.AreaArmyUnit
import org.scalatest.Suites
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers
import it.unibo.warverse.domain.model.fight.Fight.AttackAction.*

object ArmyTest:

  given Environment = DomainExample.environment

  given TargetFinderStrategy.TargetFinderStrategy2D =
    TargetFinderStrategy.TargetFinderStrategy2D()
  class AreaArmyUnitTest extends AnyFunSuite with Matchers:
    import DomainExample.Army.AreaArmyUnits.*
    test("A unit with no target in range should attack nobody") {
      distantUnit.attack() mustBe Seq.empty
    }

    test("An attack of a units with 100% chance of hit should be successful") {
      successfulUnit.attack() mustBe Seq(
        AreaAttackAction(
          countryB.boundaries.center,
          successfulUnit.areaOfImpact
        )
      )
    }

    test("An attack of a units with 0% chance of hit should fail") {
      failingUnit.attack() mustBe Seq.empty
    }

  class PrecisionArmyUnitTest extends AnyFunSuite with Matchers:
    import Army.PrecisionArmyUnits.*

    test("A unit with no target in range should attack nobody") {
      distantUnit.attack() mustBe Seq.empty
    }

    test(
      "An attack of a units with 100% chance of hit should attack the nearest enemy unit"
    ) {
      successfulUnit.attack() mustBe Seq(
        PrecisionAttackAction(
          Army.AreaArmyUnits.successfulUnit.position
        )
      )
    }

    test("An attack of a units with 0% chance of hit should fail") {
      failingUnit.attack() mustBe Seq.empty
    }

class ArmyTest
    extends Suites(
      ArmyTest.AreaArmyUnitTest(),
      ArmyTest.PrecisionArmyUnitTest()
    )
