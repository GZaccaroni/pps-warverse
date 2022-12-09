package it.unibo.warverse.data.models

import it.unibo.warverse.domain.model.common.validation.CommonValidators.*
import it.unibo.warverse.domain.model.common.validation.Validation.*

private[data] object GeometryDtos:
  case class Point2DDto(x: Double, y: Double) extends Validatable:
    override def validationErrors: List[ValidationError] =
      (x must BeGreaterThanOrEqualTo(0.0)) :::
        (y must BeGreaterThanOrEqualTo(0.0))
