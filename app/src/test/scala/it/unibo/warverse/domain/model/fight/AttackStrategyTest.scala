package it.unibo.warverse.domain.model.fight

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.common.DomainExample.{
  countryA,
  countryB,
  countryC,
  environment
}
import it.unibo.warverse.domain.model.fight.AttackStrategy.AttackStrategy2D
import it.unibo.warverse.domain.model.fight.Fight.AttackType
import org.scalatest.Suites
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

object AttackStrategyTest:
  class AttackStrategy2DTest extends AnyFunSuite with Matchers:
    test("An AttackStrategy2D should be build correctly") {
      AttackStrategy.attackStrategy2D(
        environment,
        countryA.id
      ) mustBe AttackStrategy2D(Seq(countryB))
    }

    test(
      "The targets of Area AttackType should be the centers of enemies countries"
    ) {
      val strategy = AttackStrategy.attackStrategy2D(environment, countryA.id)
      strategy.attackTargets(AttackType.Area) mustBe Seq(
        countryB.boundaries.center
      )
    }

    test(
      "The targets of Precision Attack type should be the positions of enemies army units"
    ) {
      val strategy = AttackStrategy.attackStrategy2D(environment, countryA.id)
      strategy.attackTargets(AttackType.Precision) mustBe countryB.armyUnits
        .map(_.position)
    }

class AttackStrategyTest
    extends Suites(
      AttackStrategyTest.AttackStrategy2DTest()
    )
