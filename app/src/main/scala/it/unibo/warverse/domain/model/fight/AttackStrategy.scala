package it.unibo.warverse.domain.model.fight

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.fight.Fight
import it.unibo.warverse.domain.model.common.Geometry
import it.unibo.warverse.domain.model.world.World.{Country, CountryId}

trait AttackStrategy:
  type AttackPosition

  def attackTargets(
    attackType: Fight.AttackType
  ): Seq[AttackPosition]

object AttackStrategy:
  def attackStrategy2D(
    environment: Environment,
    myCountry: CountryId
  ): AttackStrategy2D =
    AttackStrategy2D(
      environment.countries.filter(country =>
        environment.interstateRelations
          .countryEnemies(myCountry)
          .toSet
          .contains(country.id)
      )
    )

  trait AttackStrategy2D extends AttackStrategy:
    override type AttackPosition = Geometry.Point2D

  object AttackStrategy2D:
    def apply(countryEnemies: Iterable[Country]): AttackStrategy2D =
      AttackStrategy2dImpl(countryEnemies)
    private case class AttackStrategy2dImpl(countryEnemies: Iterable[Country])
        extends AttackStrategy2D:
      override def attackTargets(
        attackType: Fight.AttackType
      ): List[Geometry.Point2D] = attackType match
        case Fight.AttackType.Area =>
          countryEnemies.map(_.boundaries.center).toList
        case Fight.AttackType.Precision =>
          countryEnemies.flatMap(_.armyUnits.map(_.position)).toList
