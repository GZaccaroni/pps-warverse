package it.unibo.warverse.data.models

import it.unibo.warverse.data.models.GeometryDtos.Point2DDto
import it.unibo.warverse.domain.model.common.Math
import it.unibo.warverse.domain.model.common.validation.CommonValidators.*
import it.unibo.warverse.domain.model.common.validation.Validation.*

private[data] object ArmyDtos:
  case class CountryArmy(
    unitKinds: Seq[ArmyUnitKind],
    units: Seq[ArmyUnit]
  ) extends Validatable:
    override def validationErrors: List[ValidationError] =
      val unitKindsIdentifiers = unitKinds.map(_.id)
      (units.forall(unit =>
        unitKindsIdentifiers.contains(unit.kind)
      ) orElse "Some units have an undefined kind") :::
        (unitKindsIdentifiers must ContainNoDuplicates()) :::
        unitKinds.validationErrors :::
        units.validationErrors

  sealed trait ArmyUnitKind extends Validatable:
    def id: String
    def name: String
    def speed: Double
    def hitChance: Math.Percentage
    def hitRange: Double
    def maximumHits: Int
    def dailyResourcesUsage: Double
    def attackType: UnitAttackType

    override def validationErrors: List[ValidationError] =
      (speed must BeGreaterThanOrEqualTo(0.0)) :::
        (hitChance must BeIncludedInRange(0.0, 100.0)) :::
        (maximumHits must BeGreaterThanOrEqualTo(0)) :::
        (dailyResourcesUsage must BeGreaterThanOrEqualTo(0.0))

  object ArmyUnitKind:
    case class PrecisionArmyUnitKind(
      id: String,
      name: String,
      speed: Double,
      hitChance: Math.Percentage,
      hitRange: Double,
      maximumHits: Int,
      dailyResourcesUsage: Double
    ) extends ArmyUnitKind:
      override def attackType: UnitAttackType = UnitAttackType.Precision
    case class AreaArmyUnitKind(
      id: String,
      name: String,
      speed: Double,
      hitChance: Math.Percentage,
      hitRange: Double,
      maximumHits: Int,
      dailyResourcesUsage: Double,
      damageArea: Double
    ) extends ArmyUnitKind:
      override def attackType: UnitAttackType = UnitAttackType.Area

      override def validationErrors: List[ValidationError] =
        super.validationErrors :::
          (damageArea must BeGreaterThanOrEqualTo(0.0))

  case class ArmyUnit(kind: String, position: Point2DDto) extends Validatable:
    override def validationErrors: List[ValidationError] =
      position.validationErrors

  enum UnitAttackType(val rawValue: String):
    case Precision extends UnitAttackType("precision")
    case Area extends UnitAttackType("area")
  object UnitAttackType:
    def apply(value: String): Option[UnitAttackType] =
      value match
        case UnitAttackType.Precision.rawValue => Some(UnitAttackType.Precision)
        case UnitAttackType.Area.rawValue      => Some(UnitAttackType.Area)
        case _                                 => None
