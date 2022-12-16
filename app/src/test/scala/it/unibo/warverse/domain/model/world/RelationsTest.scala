package it.unibo.warverse.domain.model.world

import it.unibo.warverse.domain.model.common.DomainExample.{
  countryA,
  countryB,
  countryC
}
import it.unibo.warverse.domain.model.common.Geometry
import it.unibo.warverse.domain.model.common.Geometry.Point2D
import it.unibo.warverse.domain.model.world.Relations.*
import it.unibo.warverse.domain.model.world.World.Country
import org.scalatest.{BeforeAndAfter, Suites}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

object RelationsTest:
  class InterCountryRelationsTest extends AnyFunSuite with Matchers:

    test("When a relation is added it must compare in the new object") {
      val interCountryRelations: InterCountryRelations =
        InterCountryRelations(Set.empty)
      val AWarB = ((countryA.id, countryB.id), RelationStatus.WAR)
      val BAlliedC = ((countryB.id, countryC.id), RelationStatus.ALLIANCE)

      interCountryRelations.relations mustBe Set.empty
      interCountryRelations.withRelation(AWarB).relations mustBe Set(AWarB)
      interCountryRelations
        .withRelation(AWarB)
        .withRelation(BAlliedC)
        .relations mustBe Set(AWarB, BAlliedC)
    }

    test("Two country must have only one relation") {
      val AWarB = ((countryA.id, countryB.id), RelationStatus.WAR)
      val ANeutralB = ((countryA.id, countryB.id), RelationStatus.NEUTRAL)
      val BAlliedA = ((countryB.id, countryA.id), RelationStatus.ALLIANCE)

      val interCountryRelations: InterCountryRelations =
        InterCountryRelations(Set(AWarB))

      an[IllegalStateException] should be thrownBy interCountryRelations
        .withRelation(ANeutralB)
      an[IllegalStateException] should be thrownBy interCountryRelations
        .withRelation(BAlliedA)
      an[IllegalStateException] should be thrownBy InterCountryRelations(
        Set(AWarB, BAlliedA)
      )

    }

    test("When a relation is removed it must not compare in the new object") {
      val AWarB = ((countryA.id, countryB.id), RelationStatus.WAR)
      val BAlliedC = ((countryB.id, countryC.id), RelationStatus.ALLIANCE)
      val ANeutralC = ((countryA.id, countryC.id), RelationStatus.NEUTRAL)
      val interCountryRelations: InterCountryRelations =
        InterCountryRelations(Set(AWarB, BAlliedC, ANeutralC))

      interCountryRelations.relations mustBe Set(AWarB, BAlliedC, ANeutralC)
      interCountryRelations.withoutRelation(AWarB).relations mustBe Set(
        BAlliedC,
        ANeutralC
      )
      interCountryRelations
        .withoutRelation(AWarB)
        .withoutRelation(BAlliedC)
        .withoutRelation(ANeutralC)
        .relations mustBe Set.empty
    }

    test("Function countryAllies must get ALLIANCE related country") {
      val AWarB = ((countryA.id, countryB.id), RelationStatus.WAR)
      val BAlliedC = ((countryB.id, countryC.id), RelationStatus.ALLIANCE)
      val CAlliedA = ((countryC.id, countryA.id), RelationStatus.ALLIANCE)
      val interCountryRelations: InterCountryRelations =
        InterCountryRelations(Set(AWarB, BAlliedC, CAlliedA))

      interCountryRelations countryAllies countryC.id must contain only (countryA.id, countryB.id)
      interCountryRelations countryAllies countryB.id must contain only countryC.id
      interCountryRelations.withoutRelation(
        BAlliedC
      ) countryAllies countryB.id mustBe empty
    }

    test("Function countryEnemies must get WAR related country") {
      val AWarB = ((countryA.id, countryB.id), RelationStatus.WAR)
      val BAlliedC = ((countryB.id, countryC.id), RelationStatus.ALLIANCE)
      val ANeutralC = ((countryC.id, countryA.id), RelationStatus.ALLIANCE)
      val interCountryRelations: InterCountryRelations =
        InterCountryRelations(Set(AWarB, BAlliedC, ANeutralC))

      interCountryRelations countryEnemies countryC.id mustBe empty
      interCountryRelations countryEnemies countryA.id must contain only countryB.id
      interCountryRelations countryEnemies countryB.id must contain only countryA.id
    }

class RelationsTest extends Suites(RelationsTest.InterCountryRelationsTest())
