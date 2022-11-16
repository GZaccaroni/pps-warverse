package it.unibo.warverse.model.world

import it.unibo.warverse.model.common.Geometry
import it.unibo.warverse.model.common.Geometry.{Point2D, Polygon2D}
import it.unibo.warverse.model.world.InterstateRelations.{
  InterstateRelations,
  InterstateRelationsImpl,
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
      InterstateRelationsImpl(List.empty)
    val AWarB = (countryA, countryB, RelationStatus.WAR)
    val BAlliedC = (countryB, countryC, RelationStatus.ALLIANCE)

    interstateRelations.relations mustBe List.empty
    interstateRelations.addRelation(AWarB).relations mustBe List(AWarB)
    interstateRelations
      .addRelation(AWarB)
      .addRelation(BAlliedC)
      .relations mustBe List(AWarB, BAlliedC)
  }
