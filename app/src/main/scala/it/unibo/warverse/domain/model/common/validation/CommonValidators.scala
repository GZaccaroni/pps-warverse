package it.unibo.warverse.domain.model.common.validation

import it.unibo.warverse.domain.model.common.validation.Validation.*

import scala.math.Ordered.orderingToOrdered

object CommonValidators:

  case class GreaterThanOrEqualTo[Value: Ordering](
    value: Value
  ) extends Validator[Value]:
    override def validate(field: ValidationPart[Value]): Unit =
      if field.value < value then
        throw new ValidationException(
          field.entity,
          s"${field.varName} must be greater or equal than $value"
        )

  case class IncludedInRange[Value: Ordering](
    min: Value,
    max: Value
  ) extends Validator[Value]:
    override def validate(field: ValidationPart[Value]): Unit =
      if field.value < min || field.value > max then
        throw new ValidationException(
          field.entity,
          s"${field.varName} must be between $min and $max"
        )
