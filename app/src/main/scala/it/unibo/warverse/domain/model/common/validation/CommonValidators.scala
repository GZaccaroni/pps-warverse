package it.unibo.warverse.domain.model.common.validation

import it.unibo.warverse.domain.model.common.validation.Validation.*

import scala.math.Ordered.orderingToOrdered

object CommonValidators:

  case class BeGreaterThanOrEqualTo[Value: Ordering](
    value: Value
  ) extends Validator[Value]:
    override def validate(
      field: ValidationPart[Value]
    ): List[ValidationError] =
      if field.value < value then
        List(
          new ValidationError(
            field.entity,
            s"${field.varName} must be greater or equal than $value"
          )
        )
      else List.empty
  case class BeIncludedInRange[Value: Ordering](
    min: Value,
    max: Value
  ) extends Validator[Value]:
    override def validate(
      field: ValidationPart[Value]
    ): List[ValidationError] =
      if field.value < min || field.value > max then
        List(
          new ValidationError(
            field.entity,
            s"${field.varName} must be between $min and $max"
          )
        )
      else List.empty

  case class NotContainItem[Item](item: Item) extends Validator[Seq[Item]]:
    override def validate(
      field: ValidationPart[Seq[Item]]
    ): List[ValidationError] =
      if field.value.contains(item) then
        List(
          new ValidationError(
            field.entity,
            s"${field.varName} must not contain $item"
          )
        )
      else List.empty
  case class ContainNoDuplicates[Item]() extends Validator[Iterable[Item]]:
    override def validate(
      field: ValidationPart[Iterable[Item]]
    ): List[ValidationError] =
      if field.value.toSet.size != field.value.size then
        List(
          new ValidationError(
            field.entity,
            s"${field.varName} must not contain any duplicate entry"
          )
        )
      else List.empty
