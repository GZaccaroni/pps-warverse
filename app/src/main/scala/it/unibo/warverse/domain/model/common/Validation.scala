package it.unibo.warverse.domain.model.common

object Validation:

  /** An entity that can be validated */
  trait Validatable:
    @throws(classOf[ValidationException])
    def validate(): Unit

  /** An entity that is validated */
  case class ValidatableEntity(name: String)

  /** An exception occurred during the validation of a {@link Validatable}
    * entity
    * @param entity
    *   the entity which is being validated
    * @param message
    *   a user-friendly message containing more informations about the exception
    */
  class ValidationException(entity: ValidatableEntity, message: String)
      extends RuntimeException(
        s"Failed validation of ${entity.name}: $message"
      )
  object ValidationException:
    /** Factory to create a new ValidationException
      * @param message
      *   the entity which is being validated
      * @param entity
      *   a user-friendly message containing more informations about the
      *   exception
      * @return
      *   An instance of {@link ValidationException}
      */
    def apply(message: String)(using
      entity: ValidatableEntity
    ): ValidationException = new ValidationException(entity, message)

  extension (field: String)

    /** A utility method for throwing exceptions when a number is not greater or
      * equal than another in a more idiomatic way
      */
    def isNotGreaterOrEqualThan(
      number: Int
    )(using entity: ValidatableEntity): ValidationException =
      new ValidationException(
        entity,
        s"$field must be greater or equal than $number"
      )

    /** A utility method for throwing exceptions when a number is not in a given
      * range in a more idiomatic way
      */
    def isNotInRange(
      range: Range
    )(using entity: ValidatableEntity): ValidationException =
      new ValidationException(
        entity,
        s"$field must be in range $range"
      )
