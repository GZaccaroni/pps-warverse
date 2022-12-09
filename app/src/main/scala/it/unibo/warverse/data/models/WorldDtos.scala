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
    override def validate(): Unit =
      resources mustBe GreaterThanOrEqualTo(0.0)
      val boundariesCount = boundaries.length
      boundariesCount mustBe GreaterThanOrEqualTo(3)
      citizens mustBe GreaterThanOrEqualTo(0)
      army.validate()

  case class CountryRelationsDto(
    allies: Seq[World.CountryId] = Seq.empty,
    enemies: Seq[World.CountryId] = Seq.empty
  )
