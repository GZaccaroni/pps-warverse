package it.unibo.warverse.data.models

import it.unibo.warverse.data.models.GeometryDtos
import it.unibo.warverse.data.models.ArmyDtos
import it.unibo.warverse.domain.model.common.validation.CommonValidators.*
import it.unibo.warverse.domain.model.world.World
import it.unibo.warverse.domain.model.common.validation.Validation.*

private[data] object WorldDtos:
  case class CountryDto(
    id: World.CountryId,
    resources: Double,
    boundaries: Seq[GeometryDtos.Point2DDto],
    citizens: Int,
    relations: CountryRelationsDto = CountryRelationsDto(),
    army: ArmyDtos.CountryArmy
  ) extends Validatable:
    override def validationErrors: List[ValidationError] =
      val boundariesCount = boundaries.length
      val relationships = relations.allies ++ relations.enemies
      (resources must BeGreaterThanOrEqualTo(0.0)) :::
        (citizens must BeGreaterThanOrEqualTo(0)) :::
        (boundariesCount must BeGreaterThanOrEqualTo(3)) :::
        (relationships must NotContainItem(id)) :::
        army.validationErrors

  case class CountryRelationsDto(
    allies: Seq[World.CountryId] = Seq.empty,
    enemies: Seq[World.CountryId] = Seq.empty
  )
