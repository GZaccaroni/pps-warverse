package it.unibo.warverse.domain.model.fight

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.common.DomainExample.{
  countryA,
  countryB,
  countryC,
  environment
}
import it.unibo.warverse.domain.model.fight.TargetFinderStrategy.TargetFinderStrategy2D
import it.unibo.warverse.domain.model.fight.Fight.AttackType
import org.scalatest.Suites
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

object TargetFinderStrategyTest:
  class TargetFinderStrategy2DTest extends AnyFunSuite with Matchers:
    given Environment = environment

    test(
      "A country that isn't in war should have no targets"
    ) {
      val strategy = TargetFinderStrategy.attackStrategy2D()
      strategy.findTargets(countryC.id, AttackType.Area) mustBe Seq.empty
    }

    test(
      "The targets of Area AttackType should be the centers of enemies countries"
    ) {
      val strategy = TargetFinderStrategy.attackStrategy2D()
      strategy.findTargets(countryA.id, AttackType.Area) mustBe Seq(
        countryB.boundaries.center
      )
      strategy.findTargets(countryB.id, AttackType.Area) mustBe Seq(
        countryA.boundaries.center
      )
    }

    test(
      "The targets of Precision Attack type should be the positions of enemies army units"
    ) {
      val strategy = TargetFinderStrategy.attackStrategy2D()
      strategy.findTargets(
        countryA.id,
        AttackType.Precision
      ) mustBe countryB.armyUnits
        .map(_.position)
    }

    test(
      "The targets of Precision Attack type should be none if country not in war"
    ) {
      val strategy = TargetFinderStrategy.attackStrategy2D()
      strategy.findTargets(
        countryC.id,
        AttackType.Precision
      ) mustBe Seq.empty
    }

class TargetFinderStrategyTest
    extends Suites(
      TargetFinderStrategyTest.TargetFinderStrategy2DTest()
    )
