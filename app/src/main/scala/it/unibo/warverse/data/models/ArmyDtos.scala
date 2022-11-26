package it.unibo.warverse.data.models

import it.unibo.warverse.data.models.GeometryDtos.Point2DDto
import it.unibo.warverse.domain.model.common.Math
import it.unibo.warverse.domain.model.common.Validation.*

object ArmyDtos:
  case class CountryArmy(
    unitKinds: List[ArmyUnitKind],
    units: List[ArmyUnit]
  ) extends Validatable:
    override def validate(): Unit =
      given ValidatedEntity = ValidatedEntity(this.getClass.getTypeName)
      if units.exists(unit => !unitKinds.exists(unit.kind == _.id)) then
        throw ValidationException("Some units have an undefined kind")
      unitKinds.foreach(_.validate())
      units.foreach(_.validate())
      if unitKinds.map(_.id).toSet.size != unitKinds.size then
        throw ValidationException(
          s"Some unit kinds have the same id"
        )

  sealed trait ArmyUnitKind extends Validatable:
    def id: String
    def name: String
    def speed: Double
    def hitChance: Math.Percentage
    def hitRange: Double
    def maximumHits: Int
    def dailyResourcesUsage: Double
    def attackType: UnitAttackType

    override def validate(): Unit =
      given ValidatedEntity = ValidatedEntity(this.getClass.getTypeName)
      if speed < 0 then throw "speed" isNotGreaterOrEqualThan 0
      val hitChanceRange = 0 to 100
      if !hitChanceRange.contains(hitChance) then
        throw "hitChanceRange" isNotInRange hitChanceRange
      if maximumHits < 0 then throw "maximumHits" isNotGreaterOrEqualThan 0
      if dailyResourcesUsage < 0 then
        throw "dailyResourcesUsage" isNotGreaterOrEqualThan 0

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

      override def validate(): Unit =
        given ValidatedEntity = ValidatedEntity(this.getClass.getTypeName)
        super.validate()
        if damageArea < 0 then throw "damageArea" isNotGreaterOrEqualThan 0

  case class ArmyUnit(kind: String, position: Point2DDto) extends Validatable:
    override def validate(): Unit =
      position.validate()

  enum UnitAttackType(val rawValue: String):
    case Precision extends UnitAttackType("precision")
    case Area extends UnitAttackType("area")
  object UnitAttackType:
    def apply(value: String): Option[UnitAttackType] =
      value match
        case UnitAttackType.Precision.rawValue => Some(UnitAttackType.Precision)
        case UnitAttackType.Area.rawValue      => Some(UnitAttackType.Area)
        case _                                 => None
