package it.unibo.warverse.data.models

import it.unibo.warverse.domain.model.common.Validation.*

private[data] object GeometryDtos:
  case class Point2DDto(x: Double, y: Double) extends Validatable:
    override def validate(): Unit =
      given ValidatableEntity = ValidatableEntity(this.getClass.getTypeName)
      if x < 0 then throw "x" isNotGreaterOrEqualThan 0
      if y < 0 then throw "y" isNotGreaterOrEqualThan 0
