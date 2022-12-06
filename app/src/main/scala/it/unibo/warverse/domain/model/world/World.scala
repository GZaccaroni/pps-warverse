package it.unibo.warverse.domain.model.world

import it.unibo.warverse.domain.model.common.Life
import it.unibo.warverse.domain.model.common.{Geometry, Life, Movement}
import it.unibo.warverse.domain.model.fight.Army.ArmyUnit
import it.unibo.warverse.domain.model.fight.Fight

object World:
  type CountryId = String

  trait UpdateResources:
    def addingResources(newResources: Life.Resources): Country
    def addingCitizens(newCitizen: Int): Country
    def addingArmyUnits(newArmy: Seq[ArmyUnit]): Country

  case class Country(
    id: CountryId,
    name: String,
    citizens: Int,
    armyUnits: Seq[ArmyUnit],
    resources: Life.Resources,
    boundaries: Geometry.Polygon2D
  ) extends UpdateResources:
    override def addingResources(newResources: Life.Resources): Country =
      if resources + newResources < 0 then this.copy(resources = 0)
      else this.copy(resources = resources + newResources)

    override def addingCitizens(newCitizen: Int): Country =
      if citizens + newCitizen < 0 then this.copy(citizens = 0)
      this.copy(citizens = citizens + newCitizen)

    override def addingArmyUnits(newArmy: Seq[ArmyUnit]): Country =
      this.copy(armyUnits = armyUnits.concat(newArmy))
