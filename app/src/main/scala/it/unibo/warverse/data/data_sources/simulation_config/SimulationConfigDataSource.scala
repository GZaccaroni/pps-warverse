package it.unibo.warverse.data.data_sources.simulation_config

import it.unibo.warverse.data.models.ArmyDtos.{ArmyUnitKind, UnitAttackType}
import it.unibo.warverse.data.models.SimulationConfigDtos.SimulationConfigDto
import it.unibo.warverse.domain.model.common.Geometry
import it.unibo.warverse.domain.model.common.Geometry.{
  Point2D,
  Polygon,
  Polygon2D
}
import it.unibo.warverse.domain.model.fight.Army
import it.unibo.warverse.domain.model.world.World
import it.unibo.warverse.data.data_sources.simulation_config.Serializers.*
import it.unibo.warverse.data.models.{ArmyDtos, GeometryDtos}
import it.unibo.warverse.data.models.WorldDtos.CountryDto
import it.unibo.warverse.domain.model.SimulationConfig
import it.unibo.warverse.domain.model.world.Relations.*
import org.json4s.*
import org.json4s.jackson.JsonMethods.*

import javax.swing.JOptionPane
import scala.language.postfixOps
import scala.util.control.Breaks.*
import java.io.File
import java.text.ParseException

trait SimulationConfigDataSource:
  def simulationConfig: SimulationConfig

object SimulationConfigDataSource:

  def apply(file: File, format: Format): SimulationConfigDataSource =
    format match
      case Format.Json => JsonSimulationConfigDataSource(file)

  enum Format:
    case Json

  private class JsonSimulationConfigDataSource(file: File)
      extends SimulationConfigDataSource:

    override def simulationConfig: SimulationConfig =
      implicit val formats: Formats =
        DefaultFormats + ArmyUnitKindSerializer() + UnitAttackTypeSerializer()
      val jsonObj: JObject = parse(file).asInstanceOf[JObject]

      val simulationConfigDto =
        jsonObj.camelizeKeys.extract[SimulationConfigDto]
      val result = mapSimulationConfigDto(simulationConfigDto)
      simulationConfigDto.validate()
      result

    private def mapSimulationConfigDto(
      dto: SimulationConfigDto
    ): SimulationConfig =
      SimulationConfig(
        countries = dto.countries.map(mapCountryDto),
        interstateRelations = mapDtoToInterstateRelations(dto)
      )

    private def mapCountryDto(dto: CountryDto): World.Country =
      World.Country(
        id = dto.id,
        name = dto.id,
        resources = dto.resources,
        citizens = dto.citizens,
        boundaries = Polygon2D(dto.boundaries.map(mapPoint2DDto)),
        armyUnits = mapArmyDto(dto.id, dto.army)
      )

    private def mapDtoToInterstateRelations(
      configDto: SimulationConfigDto
    ): InterstateRelations =
      val relations = configDto.countries
        .flatMap(country =>
          country.relations.allies.map(ally =>
            ((country.id, ally), RelationStatus.ALLIANCE)
          )
            ++ country.relations.enemies.map(enemy =>
              ((country.id, enemy), RelationStatus.WAR)
            )
        )
      InterstateRelations(relations)

    private def mapArmyDto(
      countryId: World.CountryId,
      dto: ArmyDtos.CountryArmy
    ): Seq[Army.ArmyUnit] =
      dto.units.map(unit =>
        dto.unitKinds.find(_.id == unit.kind) match
          case Some(kind) =>
            mapArmyUnitDto(countryId, kind, unit)
          case None =>
            throw RuntimeException(s"Unit kind ${unit.kind} not found")
      )
    private def mapArmyUnitDto(
      countryId: World.CountryId,
      kind: ArmyDtos.ArmyUnitKind,
      unit: ArmyDtos.ArmyUnit
    ): Army.ArmyUnit =
      kind match
        case kind: ArmyUnitKind.PrecisionArmyUnitKind =>
          Army.PrecisionArmyUnit(
            countryId = countryId,
            name = kind.name,
            position = mapPoint2DDto(unit.position),
            chanceOfHit = kind.hitChance,
            rangeOfHit = kind.hitRange,
            availableHits = kind.maximumHits,
            dailyConsume = kind.dailyResourcesUsage,
            speed = kind.speed
          )
        case kind: ArmyUnitKind.AreaArmyUnitKind =>
          Army.AreaArmyUnit(
            countryId = countryId,
            name = kind.name,
            position = mapPoint2DDto(unit.position),
            chanceOfHit = kind.hitChance,
            rangeOfHit = kind.hitRange,
            availableHits = kind.maximumHits,
            dailyConsume = kind.dailyResourcesUsage,
            speed = kind.speed,
            areaOfImpact = kind.damageArea
          )

    private def mapPoint2DDto(dto: GeometryDtos.Point2DDto): Geometry.Point2D =
      Geometry.Point2D(
        x = dto.x,
        y = dto.y
      )
