package it.unibo.warverse.domain.model.common.validation

import it.unibo.warverse.domain.model.common.validation.CommonValidators.BeGreaterThanOrEqualTo
import it.unibo.warverse.domain.model.common.validation.Validation.{
  ValidatableEntity,
  must
}
import org.scalatest.Suites
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.EitherValues.*

object ValidationTest:
  class ValidatableTest() extends AnyFunSuite with Matchers:

    test(
      "Validate must succeed if there are no validation errors"
    ) {
      val testImpl = ValidatableTestImpl(false)
      testImpl.validate().isRight shouldBe true
    }

    test(
      "Validate must fail if there is a validation error"
    ) {
      val testImpl = ValidatableTestImpl(true)
      testImpl.validate().isLeft shouldBe true
      testImpl.validate().left.value.size shouldBe 1
    }

    private class ValidatableTestImpl(
      val shouldReturnValidationError: Boolean
    ) extends Validation.Validatable:
      override def validationErrors: List[Validation.ValidationError] =
        if shouldReturnValidationError then
          List(Validation.ValidationError("Validation error"))
        else List.empty
  class ValidatorTest() extends AnyFunSuite with Matchers:
    test(
      "Validate must fail if there is a validation error and return correct variable name"
    ) {
      given ValidatableEntity = ValidatableEntity("Test")
      val testX = 5
      val validationResult = testX must BeZero()

      validationResult.length shouldBe 1
      validationResult.head.toString should include("test X")
    }
    test(
      "Validate must not fail if there are no validation errors"
    ) {
      given ValidatableEntity = ValidatableEntity("Test")

      val testX = 0
      val validationResult = testX must BeZero()

      validationResult.isEmpty shouldBe true
    }

    /** Sample Validator that returns an error with text equal to var name
      */
    private class BeZero() extends Validation.Validator[Int]:
      override def validate(
        value: Validation.ValidationPart[Int]
      ): List[Validation.ValidationError] =
        given ValidatableEntity = ValidatableEntity("Test")
        if value.value != 0 then
          List(
            Validation.ValidationError(value.varName)
          )
        else List.empty

class ValidationTest
    extends Suites(
      ValidationTest.ValidatableTest(),
      ValidationTest.ValidatorTest()
    )
