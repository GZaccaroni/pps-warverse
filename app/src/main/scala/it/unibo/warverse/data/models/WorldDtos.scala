package it.unibo.warverse.data.models

import it.unibo.warverse.data.models.GeometryDtos
import it.unibo.warverse.data.models.ArmyDtos
import it.unibo.warverse.domain.model.world.World

object WorldDtos:
  case class CountryDto(
    id: World.CountryId,
    resources: Double,
    boundaries: List[GeometryDtos.Point2DDto],
    citizens: Int,
    relations: CountryRelationsDto = CountryRelationsDto(),
    army: ArmyDtos.CountryArmy
  )

  case class CountryRelationsDto(
    allies: List[World.CountryId] = List.empty,
    enemies: List[World.CountryId] = List.empty
  )
