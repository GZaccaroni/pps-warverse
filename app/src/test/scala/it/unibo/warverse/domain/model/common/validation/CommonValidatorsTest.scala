package it.unibo.warverse.domain.model.common.validation

import it.unibo.warverse.domain.model.common.validation.CommonValidators.{
  BeGreaterThanOrEqualTo,
  BeIncludedInRange,
  ContainNoDuplicates,
  NotContainItem
}
import it.unibo.warverse.domain.model.common.validation.Validation.{
  ValidatableEntity,
  Validator,
  must
}
import org.scalatest.Suites
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.EitherValues.*

object CommonValidatorsTest:
  class BeGreaterThanOrEqualToTest()
      extends ValidatorTest[Double](
        correctSamples = List(5, 10, 15),
        wrongSamples = List(-5, 0, 1, 2),
        validator = BeGreaterThanOrEqualTo(5)
      )

  class BeIncludedInRangeTest()
      extends ValidatorTest[Double](
        correctSamples = List(0, 2, 3, 8),
        wrongSamples = List(-5, -1, 12, 15),
        validator = BeIncludedInRange(0, 10)
      )

  class NotContainItemTest()
      extends ValidatorTest[Seq[Int]](
        correctSamples = List(List(0, 1, 2), List(1, 2, 3), List.empty),
        wrongSamples = List(List(1, 2, 5), List(5)),
        validator = NotContainItem(5)
      )

  class ContainNoDuplicatesTest()
      extends ValidatorTest[Iterable[Int]](
        correctSamples = List(List(1, 5), List(10, 15)),
        wrongSamples = List(List(1, 1), List(1, 2, 1)),
        validator = ContainNoDuplicates[Int]()
      )

  class ValidatorTest[T](
    val correctSamples: List[T],
    val wrongSamples: List[T],
    val validator: Validator[T]
  ) extends AnyFunSuite
      with Matchers:
    given ValidatableEntity = ValidatableEntity("TestEntity")
    for correctSample <- correctSamples do
      val validationResult = correctSample must validator
      validationResult.isEmpty shouldBe true
    for wrongSample <- wrongSamples do
      val validationResult = wrongSample must validator
      validationResult.isEmpty shouldBe false

class CommonValidatorsTest
    extends Suites(
      CommonValidatorsTest.BeGreaterThanOrEqualToTest()
    )
