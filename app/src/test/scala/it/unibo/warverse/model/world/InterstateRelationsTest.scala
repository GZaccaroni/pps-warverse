package it.unibo.warverse.domain.model.world

import it.unibo.warverse.domain.model.common.Geometry
import it.unibo.warverse.domain.model.common.Geometry.{Point2D, Polygon2D}
import it.unibo.warverse.domain.model.world.Relations.{
  InterstateRelations,
  RelationStatus
}
import it.unibo.warverse.domain.model.world.World.Country
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

class InterstateRelationsTest extends AnyFunSuite with Matchers:
  private val countryA: Country =
    Country("ID_1", "A", 40, List.empty, 0.0, Polygon2D(List.empty))
  private val countryB: Country =
    Country("ID_2", "B", 10, List.empty, 0.0, Polygon2D(List.empty))
  private val countryC: Country =
    Country("ID_3", "C", 20, List.empty, 0.0, Polygon2D(List.empty))

  test("When a relation is added it must compare in the new object") {
    val interstateRelations: InterstateRelations =
      InterstateRelations(List.empty)
    val AWarB = ((countryA.id, countryB.id), RelationStatus.WAR)
    val BAlliedC = ((countryB.id, countryC.id), RelationStatus.ALLIANCE)

    interstateRelations.relations mustBe List.empty
    interstateRelations.withRelation(AWarB).relations mustBe List(AWarB)
    interstateRelations
      .withRelation(AWarB)
      .withRelation(BAlliedC)
      .relations mustBe List(AWarB, BAlliedC)
  }

  test("Two country must have only one relation") {
    val AWarB = ((countryA.id, countryB.id), RelationStatus.WAR)
    val ANeutralB = ((countryA.id, countryB.id), RelationStatus.NEUTRAL)
    val BAlliedA = ((countryB.id, countryA.id), RelationStatus.ALLIANCE)

    val interstateRelations: InterstateRelations =
      InterstateRelations(List(AWarB))

    an[IllegalStateException] should be thrownBy interstateRelations
      .withRelation(ANeutralB)
    an[IllegalStateException] should be thrownBy interstateRelations
      .withRelation(BAlliedA)
    an[IllegalStateException] should be thrownBy InterstateRelations(
      List(AWarB, BAlliedA)
    )

  }

  test("When a relation is removed it must not compare in the new object") {
    val AWarB = ((countryA.id, countryB.id), RelationStatus.WAR)
    val BAlliedC = ((countryB.id, countryC.id), RelationStatus.ALLIANCE)
    val ANeutralC = ((countryA.id, countryC.id), RelationStatus.NEUTRAL)
    val interstateRelations: InterstateRelations =
      InterstateRelations(List(AWarB, BAlliedC, ANeutralC))

    interstateRelations.relations mustBe List(AWarB, BAlliedC, ANeutralC)
    interstateRelations.withoutRelation(AWarB).relations mustBe List(
      BAlliedC,
      ANeutralC
    )
    interstateRelations
      .withoutRelation(AWarB)
      .withoutRelation(BAlliedC)
      .withoutRelation(ANeutralC)
      .relations mustBe List.empty
  }

  test("Function getAllies must get ALLIANCE related country") {
    val AWarB = ((countryA.id, countryB.id), RelationStatus.WAR)
    val BAlliedC = ((countryB.id, countryC.id), RelationStatus.ALLIANCE)
    val CAlliedA = ((countryC.id, countryA.id), RelationStatus.ALLIANCE)
    val interstateRelations: InterstateRelations =
      InterstateRelations(List(AWarB, BAlliedC, CAlliedA))

    interstateRelations getAllies countryC.id must contain only (countryA.id, countryB.id)
    interstateRelations getAllies countryB.id must contain only countryC.id
    interstateRelations.withoutRelation(
      BAlliedC
    ) getAllies countryB.id mustBe empty
  }

  test("Function getWars must get WAR related country") {
    val AWarB = ((countryA.id, countryB.id), RelationStatus.WAR)
    val BAlliedC = ((countryB.id, countryC.id), RelationStatus.ALLIANCE)
    val ANeutralC = ((countryC.id, countryA.id), RelationStatus.ALLIANCE)
    val interstateRelations: InterstateRelations =
      InterstateRelations(List(AWarB, BAlliedC, ANeutralC))

    interstateRelations getEnemies countryC.id mustBe empty
    interstateRelations getEnemies countryA.id must contain only countryB.id
    interstateRelations getEnemies countryB.id must contain only countryA.id
  }
