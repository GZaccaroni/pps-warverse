package it.unibo.warverse.domain.model.world

import it.unibo.warverse.domain.model.common.Life
import it.unibo.warverse.domain.model.common.{Geometry, Life, Movement}
import it.unibo.warverse.domain.model.fight.Army.ArmyUnit
import it.unibo.warverse.domain.model.fight.Fight

object World:
  type CountryId = String

  trait UpdatableAssets[UpdatableType <: UpdatableAssets[UpdatableType]]:
    /** It returns a new entity with the given resources added
      * @param newResources
      *   the resources to be added
      * @return
      *   a new instance of {@link UpdatableType} with the resources added
      */
    def addingResources(newResources: Life.Resources): UpdatableType

    /** It returns a new entity with the given citizens added
      *
      * @param newCitizens
      *   the citizens to be added
      * @return
      *   a new instance of {@link UpdatableType} with the citizens added
      */
    def addingCitizens(newCitizens: Int): UpdatableType

    /** It returns a new entity with the given army units added
      *
      * @param newArmyUnits
      *   the army units to be added
      * @return
      *   a new instance of {@link UpdatableType} with the army units added
      */
    def addingArmyUnits(newArmyUnits: Seq[ArmyUnit]): UpdatableType

  /** A country
    * @param id
    *   The identifier of the country
    * @param name
    *   The name of the country
    * @param citizens
    *   The number of citizens in the country
    * @param armyUnits
    *   The army units in the country
    * @param resources
    *   The quantity of resources of the country
    * @param boundaries
    *   The boundaries of the country
    */
  case class Country(
    id: CountryId,
    name: String,
    citizens: Int,
    armyUnits: Seq[ArmyUnit],
    resources: Life.Resources,
    boundaries: Geometry.Polygon2D
  ) extends UpdatableAssets[Country]:
    /** It returns a new {@link Country} with the given resources added
      * @param newResources
      *   the resources to be added
      * @return
      *   a new instance of {@link Country} with the resources added
      */
    override def addingResources(newResources: Life.Resources): Country =
      if resources + newResources < 0 then this.copy(resources = 0)
      else this.copy(resources = resources + newResources)

    /** It returns a new {@link Country} with the given citizens added
      * @param newCitizens
      *   the citizens to be added
      * @return
      *   a new instance of {@link Country} with the citizens added
      */
    override def addingCitizens(newCitizens: Int): Country =
      if citizens + newCitizen < 0 then this.copy(citizens = 0)
      this.copy(citizens = citizens + newCitizens)

    /** * It returns a new {@link Country} with the given army units added
      * @param newArmyUnits
      *   the army units to be added
      * @return
      *   a new instance of {@link Country} with the army units added
      */
    override def addingArmyUnits(newArmyUnits: Seq[ArmyUnit]): Country =
      this.copy(armyUnits = newArmyUnits.concat(newArmy))
