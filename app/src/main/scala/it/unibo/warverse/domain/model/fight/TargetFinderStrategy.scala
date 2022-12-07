package it.unibo.warverse.domain.model.fight

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.fight.Fight
import it.unibo.warverse.domain.model.common.Geometry
import it.unibo.warverse.domain.model.world.World.{Country, CountryId}
object TargetFinderStrategy:
  /** It represent a generic target finder strategy
    * @tparam AttackPosition
    *   the type that represent the position of the attack
    */
  trait TargetFinderStrategy[AttackPosition]:
    /** A function that compute the target of a Country for a specified attack
      * type
      * @param ofCountry
      *   the country that search possible target
      * @param attackType
      *   the type of the attack
      * @return
      *   a sequence of position of possible attack position
      */
    def findTargets(
      ofCountry: CountryId,
      attackType: Fight.AttackType
    ): Seq[AttackPosition]

  type TargetFinderStrategy2D = TargetFinderStrategy[Geometry.Point2D]

  /** Factory for [[TargetFinderStrategy]] instance with Point2D AttackPosition
    */
  object TargetFinderStrategy2D:

    /** Creates a TargetFinderStrategy2D with the given environment
      * @param environment
      *   the environment in which create the strategy
      * @return
      *   a new TargetFinderStrategy2D instance that find target in the given
      *   environment
      */
    def apply()(using environment: Environment): TargetFinderStrategy2D =
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
          environment.interCountryRelations
            .countryEnemies(countryId)
            .toSet
            .contains(country.id)
        )
