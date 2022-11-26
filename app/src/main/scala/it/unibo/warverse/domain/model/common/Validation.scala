package it.unibo.warverse.domain.model.common

object Validation:

  trait Validatable:
    @throws(classOf[ValidationException])
    def validate(): Unit

  case class ValidatedEntity(name: String)

  class ValidationException(entity: ValidatedEntity, message: String)
      extends RuntimeException(
        s"Failed validation of ${entity.name}: $message"
      )
  object ValidationException:
    def apply(message: String)(using
      entity: ValidatedEntity
    ): ValidationException = new ValidationException(entity, message)

  extension (field: String)
    def isNotGreaterOrEqualThan(
      number: Int
    )(using entity: ValidatedEntity): ValidationException =
      new ValidationException(
        entity,
        s"$field must be greater or equal than $number"
      )

    def isNotInRange(
      range: Range
    )(using entity: ValidatedEntity): ValidationException =
      new ValidationException(
        entity,
        s"$field must be in range $range"
      )
