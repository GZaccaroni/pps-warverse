package it.unibo.warverse.data.data_sources.json

import it.unibo.warverse.data.models.ArmyDtos.{ArmyUnitKind, UnitAttackType}
import org.json4s.{MappingException, *}

object Serializers:
  class ArmyUnitKindSerializer extends Serializer[ArmyUnitKind]:
    private val MyClassClass = classOf[ArmyUnitKind]

    def deserialize(implicit
      format: Formats
    ): PartialFunction[(TypeInfo, JValue), ArmyUnitKind] = {
      case (TypeInfo(MyClassClass, _), json) =>
        val attackType = (json
          .asInstanceOf[
            JObject
          ] \ "attackType").extract[UnitAttackType]
        attackType match
          case UnitAttackType.Precision =>
            json.extract[ArmyUnitKind.PrecisionArmyUnitKind]
          case UnitAttackType.Area =>
            json.extract[ArmyUnitKind.AreaArmyUnitKind]
    }

    def serialize(implicit formats: Formats): PartialFunction[Any, JValue] = {
      case precisionKind: ArmyUnitKind.PrecisionArmyUnitKind =>
        Extraction.decompose(precisionKind)
      case areaKind: ArmyUnitKind.AreaArmyUnitKind =>
        Extraction.decompose(areaKind)
    }

  class UnitAttackTypeSerializer extends Serializer[UnitAttackType]:
    private val MyClassClass = classOf[UnitAttackType]

    def deserialize(implicit
      format: Formats
    ): PartialFunction[(TypeInfo, JValue), UnitAttackType] = {
      case (TypeInfo(MyClassClass, _), json) =>
        val rawValue = json.values.toString
        UnitAttackType(rawValue) match
          case Some(value) => value
          case None =>
            throw  MappingException(
              "Can't convert " + rawValue + " to UnitAttackType"
            )
    }

    def serialize(implicit formats: Formats): PartialFunction[Any, JValue] = {
      case value: UnitAttackType => JString(value.rawValue)
    }
