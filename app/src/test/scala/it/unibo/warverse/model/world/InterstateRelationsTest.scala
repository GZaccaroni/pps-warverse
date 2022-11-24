package it.unibo.warverse.model.world

import it.unibo.warverse.model.common.Geometry
import it.unibo.warverse.model.common.Geometry.{Point2D, Polygon2D}
import it.unibo.warverse.model.world.Relations.{
  InterstateRelations,
  RelationStatus
}
import it.unibo.warverse.model.world.World.{Citizen, Country}
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

class InterstateRelationsTest extends AnyFunSuite with Matchers:
  private val countryA: Country =
    Country("A", List.empty, List.empty, 0.0, Polygon2D(List.empty))
  private val countryB: Country =
    Country("B", List.empty, List.empty, 0.0, Polygon2D(List.empty))
  private val countryC: Country =
    Country("C", List.empty, List.empty, 0.0, Polygon2D(List.empty))

  test("When a relation is added it must compare in the new object") {
    val interstateRelations: InterstateRelations =
      InterstateRelations(List.empty)
    val AWarB = ((countryA, countryB), RelationStatus.WAR)
    val BAlliedC = ((countryB, countryC), RelationStatus.ALLIANCE)

    interstateRelations.relations mustBe List.empty
    interstateRelations.addRelation(AWarB).relations mustBe List(AWarB)
    interstateRelations
      .addRelation(AWarB)
      .addRelation(BAlliedC)
      .relations mustBe List(AWarB, BAlliedC)
  }

  test("Two country must have only one relation") {
    val AWarB = ((countryA, countryB), RelationStatus.WAR)
    val ANeutralB = ((countryA, countryB), RelationStatus.NEUTRAL)
    val BAlliedA = ((countryB, countryA), RelationStatus.ALLIANCE)

    val interstateRelations: InterstateRelations =
      InterstateRelations(List(AWarB))

    an[IllegalStateException] should be thrownBy interstateRelations
      .addRelation(ANeutralB)
    an[IllegalStateException] should be thrownBy interstateRelations
      .addRelation(BAlliedA)
    an[IllegalStateException] should be thrownBy InterstateRelations(
      List(AWarB, BAlliedA)
    )

  }

  test("When a relation is removed it must not compare in the new object") {
    val AWarB = ((countryA, countryB), RelationStatus.WAR)
    val BAlliedC = ((countryB, countryC), RelationStatus.ALLIANCE)
    val ANeutralC = ((countryA, countryC), RelationStatus.NEUTRAL)
    val interstateRelations: InterstateRelations =
      InterstateRelations(List(AWarB, BAlliedC, ANeutralC))

    interstateRelations.relations mustBe List(AWarB, BAlliedC, ANeutralC)
    interstateRelations.removeRelation(AWarB).relations mustBe List(
      BAlliedC,
      ANeutralC
    )
    interstateRelations
      .removeRelation(AWarB)
      .removeRelation(BAlliedC)
      .removeRelation(ANeutralC)
      .relations mustBe List.empty
  }

  test("Function getAllies must get ALLIANCE related country") {
    val AWarB = ((countryA, countryB), RelationStatus.WAR)
    val BAlliedC = ((countryB, countryC), RelationStatus.ALLIANCE)
    val CAlliedA = ((countryC, countryA), RelationStatus.ALLIANCE)
    val interstateRelations: InterstateRelations =
      InterstateRelations(List(AWarB, BAlliedC, CAlliedA))

    interstateRelations getAllies countryC must contain only (countryA, countryB)
    interstateRelations getAllies countryB must contain only countryC
    interstateRelations.removeRelation(BAlliedC) getAllies countryB mustBe empty
  }

  test("Function getWars must get WAR related country") {
    val AWarB = ((countryA, countryB), RelationStatus.WAR)
    val BAlliedC = ((countryB, countryC), RelationStatus.ALLIANCE)
    val ANeutralC = ((countryC, countryA), RelationStatus.ALLIANCE)
    val interstateRelations: InterstateRelations =
      InterstateRelations(List(AWarB, BAlliedC, ANeutralC))

    interstateRelations getWars countryC mustBe empty
    interstateRelations getWars countryA must contain only countryB
    interstateRelations getWars countryB must contain only countryA
  }
