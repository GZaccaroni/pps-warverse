package it.unibo.warverse.domain.model.common.validation

import com.github.dwickern.macros.NameOf.*
import com.github.dwickern.macros.NameOfImpl

object Validation:

  /** An entity that can be validated */
  trait Validatable:
    given ValidatableEntity = ValidatableEntity(this.getClass.getSimpleName)
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

  extension [Value](field: Value)
    transparent inline def mustBe(validator: Validator[Value])(using
      entity: ValidatableEntity
    ): Unit =
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
    /** @param value
      * @throws {@link
      *   ValidationException} throws a validation exception
      */
    def validate(
      value: ValidationPart[ValueType]
    ): Unit
