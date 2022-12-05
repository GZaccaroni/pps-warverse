package it.unibo.warverse.domain.model.fight

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.fight.Fight
import it.unibo.warverse.domain.model.common.Geometry
import it.unibo.warverse.domain.model.world.World.{Country, CountryId}

trait TargetFinderStrategy[AttackPosition]:
  def findTargets(
    ofCountry: CountryId,
    attackType: Fight.AttackType
  ): Seq[AttackPosition]

object TargetFinderStrategy:
  type TargetFinderStrategy2D = TargetFinderStrategy[Geometry.Point2D]
  def attackStrategy2D()(using
    environment: Environment
  ): TargetFinderStrategy2D =
    TargetFinderStrategy2D(
      environment
    )

  object TargetFinderStrategy2D:
    def apply(environment: Environment): TargetFinderStrategy2D =
      TargetFinderStrategy2DImpl(environment)
    private case class TargetFinderStrategy2DImpl(
      private val environment: Environment
    ) extends TargetFinderStrategy2D:
      override def findTargets(
        ofCountry: CountryId,
        attackType: Fight.AttackType
      ): Seq[Geometry.Point2D] = attackType match
        case Fight.AttackType.Area =>
          enemiesOfCountry(ofCountry)
            .map(_.boundaries.center)
        case Fight.AttackType.Precision =>
          enemiesOfCountry(ofCountry)
            .flatMap(_.armyUnits.map(_.position))
      private def enemiesOfCountry(countryId: CountryId): Seq[Country] =
        environment.countries.filter(country =>
          environment.interstateRelations
            .countryEnemies(countryId)
            .toSet
            .contains(country.id)
        )
