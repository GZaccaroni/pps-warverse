package it.unibo.warverse.data.repositories

import it.unibo.warverse.data.data_sources.simulation_config.SimulationConfigDataSource
import it.unibo.warverse.domain.model.SimulationConfig
import it.unibo.warverse.domain.model.common.Geometry
import it.unibo.warverse.domain.model.common.validation.Validation
import it.unibo.warverse.domain.repositories.SimulationConfigRepository
import monix.eval.Task
import it.unibo.warverse.domain.model.common.Geometry.{
  MultiPolygon,
  Point2D,
  Polygon
}
import it.unibo.warverse.data.models.SimulationConfigDtos.SimulationConfigDto
import it.unibo.warverse.data.models.{ArmyDtos, GeometryDtos}
import it.unibo.warverse.data.models.WorldDtos.CountryDto
import it.unibo.warverse.data.models.ArmyDtos.{ArmyUnitKind, UnitAttackType}
import it.unibo.warverse.domain.model.fight.Army
import it.unibo.warverse.domain.model.world.World
import it.unibo.warverse.domain.model.world.Relations.*

import java.io.File

class SimulationConfigRepositoryImpl(
  datasource: SimulationConfigDataSource
) extends SimulationConfigRepository:
  override def readSimulationConfig(
    file: File
  ): Task[Either[List[Validation.ValidationError], SimulationConfig]] =
    datasource
      .readSimulationConfig(file)
      .map(result => result.validate().map(_ => mapSimulationConfigDto(result)))

  private def mapSimulationConfigDto(
    dto: SimulationConfigDto
  ): SimulationConfig =
    SimulationConfig(
      countries = dto.countries.map(mapCountryDto),
      interCountryRelations = mapDtoToInterCountryRelations(dto)
    )

  private def mapCountryDto(dto: CountryDto): World.Country =
    World.Country(
      id = dto.id,
      name = dto.id,
      resources = dto.resources,
      citizens = dto.citizens,
      boundaries = MultiPolygon(Polygon(dto.boundaries.map(mapPoint2DDto))),
      armyUnits = mapArmyDto(dto.id, dto.army)
    )

  private def mapDtoToInterCountryRelations(
    configDto: SimulationConfigDto
  ): InterCountryRelations =
    val relations = configDto.countries
      .flatMap(country =>
        country.relations.allies.map(ally =>
          ((country.id, ally), RelationStatus.ALLIANCE)
        )
          ++ country.relations.enemies.map(enemy =>
            ((country.id, enemy), RelationStatus.WAR)
          )
      )
    InterCountryRelations(relations.toSet)

  private def mapArmyDto(
    countryId: World.CountryId,
    dto: ArmyDtos.CountryArmy
  ): Seq[Army.ArmyUnit] =
    dto.units.map(unit =>
      dto.unitKinds
        .find(_.id == unit.kind)
        .map(kind => mapArmyUnitDto(countryId, kind, unit))
        .get
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
