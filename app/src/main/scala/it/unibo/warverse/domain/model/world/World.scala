package it.unibo.warverse.domain.model.world

import it.unibo.warverse.domain.model.common.Life
import it.unibo.warverse.domain.model.fight.Fight.Attackable
import it.unibo.warverse.domain.model.common.{Geometry, Life, Movement}
import it.unibo.warverse.domain.model.fight.Army.ArmyUnit
import it.unibo.warverse.domain.model.fight.Fight
import it.unibo.warverse.domain.model.world.Relations.InterstateRelations

object World:
  type CountryId = String

  case class WorldState(
    countries: List[Country],
    interstateRelations: InterstateRelations
  )
  case class Country(
    id: CountryId,
    name: String,
    citizens: Int,
    armyUnits: List[ArmyUnit],
    resources: Life.Resources,
    boundaries: Geometry.Polygon2D
  )

  case class Citizen(
    position: Geometry.Point2D
  ) extends Movement.Locatable,
        Life.LivingEntity,
        Attackable:
    override type Position = Geometry.Point2D

    def alive: Boolean = ???