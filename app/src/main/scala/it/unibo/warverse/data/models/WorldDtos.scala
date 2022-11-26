package it.unibo.warverse.data.models

import it.unibo.warverse.data.models.GeometryDtos
import it.unibo.warverse.data.models.ArmyDtos
import it.unibo.warverse.domain.model.world.World
import it.unibo.warverse.domain.model.common.Validation.*

object WorldDtos:
  case class CountryDto(
    id: World.CountryId,
    resources: Double,
    boundaries: List[GeometryDtos.Point2DDto],
    citizens: Int,
    relations: CountryRelationsDto = CountryRelationsDto(),
    army: ArmyDtos.CountryArmy
  ) extends Validatable:
    override def validate(): Unit =
      given ValidatedEntity = ValidatedEntity(this.getClass.getTypeName)
      if resources < 0 then throw "resources" isNotGreaterOrEqualThan 0
      if boundaries.length < 3 then throw "boundaries" isNotGreaterOrEqualThan 3
      if citizens < 0 then throw "citizens" isNotGreaterOrEqualThan 0
      army.validate()

  case class CountryRelationsDto(
    allies: List[World.CountryId] = List.empty,
    enemies: List[World.CountryId] = List.empty
  )
