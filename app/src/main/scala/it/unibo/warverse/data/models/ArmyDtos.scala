package it.unibo.warverse.data.models

import it.unibo.warverse.data.models.GeometryDtos.Point2DDto
import it.unibo.warverse.domain.model.common.Math

object ArmyDtos:
  case class CountryArmy(
    unitKinds: List[ArmyUnitKind],
    units: List[ArmyUnit]
  )
  sealed trait ArmyUnitKind:
    def id: String
    def name: String
    def speed: Double
    def hitChance: Math.Percentage
    def hitRange: Double
    def maximumHits: Int
    def dailyResourcesUsage: Double
    def attackType: UnitAttackType

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

  case class ArmyUnit(kind: String, position: Point2DDto)

  enum UnitAttackType(val rawValue: String):
    case Precision extends UnitAttackType("precision")
    case Area extends UnitAttackType("area")
  object UnitAttackType:
    def apply(value: String): Option[UnitAttackType] =
      value match
        case UnitAttackType.Precision.rawValue => Some(UnitAttackType.Precision)
        case UnitAttackType.Area.rawValue      => Some(UnitAttackType.Area)
        case _                                 => None
