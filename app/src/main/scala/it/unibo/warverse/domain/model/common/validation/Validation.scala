package it.unibo.warverse.domain.model.common.validation

import com.github.dwickern.macros.NameOf.*

object Validation:

  /** An entity that can be validated */
  trait Validatable:
    given ValidatableEntity = ValidatableEntity(this.getClass.getSimpleName)
    def validate(): Either[List[ValidationError], Unit] =
      if validationErrors.isEmpty then Right(())
      else Left(validationErrors)
    def validationErrors: List[ValidationError] = List.empty

  extension (field: Iterable[Validatable])
    def validationErrors: List[ValidationError] =
      field.flatMap(_.validationErrors).toList

  /** An entity that is validated */
  case class ValidatableEntity(name: String)

  /** An exception occurred during the validation of a [[Validatable]] entity
    * @param entity
    *   the entity which is being validated
    * @param message
    *   a user-friendly message containing more informations about the exception
    */
  class ValidationError(entity: ValidatableEntity, message: String):
    override def toString: String =
      s"Failed validation of ${entity.name}: $message"
  object ValidationError:
    /** Factory to create a new ValidationException
      *
      * @param message
      *   the entity which is being validated
      * @param entity
      *   a user-friendly message containing more informations about the
      *   exception
      * @return
      *   An instance of [[ValidationError]]
      */
    def apply(message: String)(using
      entity: ValidatableEntity
    ): ValidationError = new ValidationError(entity, message)

  extension (field: Boolean)
    transparent inline def orElse(errorMessage: String)(using
      entity: ValidatableEntity
    ): List[ValidationError] =
      if !field then List(new ValidationError(entity, errorMessage))
      else List.empty

  extension [Value](field: Value)
    transparent inline def must(validator: Validator[Value])(using
      entity: ValidatableEntity
    ): List[ValidationError] =
      val part =
        ValidationPart[Value](unCamelCaseString(nameOf(field)), field, entity)
      validator.validate(part)
  private def unCamelCaseString(str: String) =
    str.replaceAll(
      String.format(
        "%s|%s|%s",
        "(?<=[A-Z])(?=[A-Z][a-z])",
        "(?<=[^A-Z])(?=[A-Z])",
        "(?<=[A-Za-z])(?=[^A-Za-z])"
      ),
      " "
    )

  case class ValidationPart[Value](
    varName: String,
    value: Value,
    entity: ValidatableEntity
  )

  trait Validator[ValueType]:
    /** Validate a value and returns a list of validation errors if there are
      * @param value
      *   the value to validate
      * @return
      *   a list of validation errors
      */
    def validate(
      value: ValidationPart[ValueType]
    ): List[ValidationError]
