package it.unibo.warverse.domain.model.world

import it.unibo.warverse.domain.model.common.DomainExample.{
  countryA,
  countryB,
  countryC
}
import it.unibo.warverse.domain.model.common.Geometry
import it.unibo.warverse.domain.model.common.Geometry.{Point2D, Polygon2D}
import it.unibo.warverse.domain.model.world.Relations.*
import it.unibo.warverse.domain.model.world.World.Country
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

class InterCountryRelationsTest extends AnyFunSuite with Matchers:

  test("When a relation is added it must compare in the new object") {
    val interstateRelations: InterCountryRelations =
      InterCountryRelations(Set.empty)
    val AWarB = ((countryA.id, countryB.id), RelationStatus.WAR)
    val BAlliedC = ((countryB.id, countryC.id), RelationStatus.ALLIANCE)

    interstateRelations.relations mustBe Set.empty
    interstateRelations.withRelation(AWarB).relations mustBe Set(AWarB)
    interstateRelations
      .withRelation(AWarB)
      .withRelation(BAlliedC)
      .relations mustBe Set(AWarB, BAlliedC)
  }

  test("Two country must have only one relation") {
    val AWarB = ((countryA.id, countryB.id), RelationStatus.WAR)
    val ANeutralB = ((countryA.id, countryB.id), RelationStatus.NEUTRAL)
    val BAlliedA = ((countryB.id, countryA.id), RelationStatus.ALLIANCE)

    val interstateRelations: InterCountryRelations =
      InterCountryRelations(Set(AWarB))

    an[IllegalStateException] should be thrownBy interstateRelations
      .withRelation(ANeutralB)
    an[IllegalStateException] should be thrownBy interstateRelations
      .withRelation(BAlliedA)
    an[IllegalStateException] should be thrownBy InterCountryRelations(
      Set(AWarB, BAlliedA)
    )

  }

  test("When a relation is removed it must not compare in the new object") {
    val AWarB = ((countryA.id, countryB.id), RelationStatus.WAR)
    val BAlliedC = ((countryB.id, countryC.id), RelationStatus.ALLIANCE)
    val ANeutralC = ((countryA.id, countryC.id), RelationStatus.NEUTRAL)
    val interstateRelations: InterCountryRelations =
      InterCountryRelations(Set(AWarB, BAlliedC, ANeutralC))

    interstateRelations.relations mustBe Set(AWarB, BAlliedC, ANeutralC)
    interstateRelations.withoutRelation(AWarB).relations mustBe Set(
      BAlliedC,
      ANeutralC
    )
    interstateRelations
      .withoutRelation(AWarB)
      .withoutRelation(BAlliedC)
      .withoutRelation(ANeutralC)
      .relations mustBe Set.empty
  }

  test("Function getAllies must get ALLIANCE related country") {
    val AWarB = ((countryA.id, countryB.id), RelationStatus.WAR)
    val BAlliedC = ((countryB.id, countryC.id), RelationStatus.ALLIANCE)
    val CAlliedA = ((countryC.id, countryA.id), RelationStatus.ALLIANCE)
    val interstateRelations: InterCountryRelations =
      InterCountryRelations(Set(AWarB, BAlliedC, CAlliedA))

    interstateRelations countryAllies countryC.id must contain only (countryA.id, countryB.id)
    interstateRelations countryAllies countryB.id must contain only countryC.id
    interstateRelations.withoutRelation(
      BAlliedC
    ) countryAllies countryB.id mustBe empty
  }

  test("Function getWars must get WAR related country") {
    val AWarB = ((countryA.id, countryB.id), RelationStatus.WAR)
    val BAlliedC = ((countryB.id, countryC.id), RelationStatus.ALLIANCE)
    val ANeutralC = ((countryC.id, countryA.id), RelationStatus.ALLIANCE)
    val interstateRelations: InterCountryRelations =
      InterCountryRelations(Set(AWarB, BAlliedC, ANeutralC))

    interstateRelations countryEnemies countryC.id mustBe empty
    interstateRelations countryEnemies countryA.id must contain only countryB.id
    interstateRelations countryEnemies countryB.id must contain only countryA.id
  }
