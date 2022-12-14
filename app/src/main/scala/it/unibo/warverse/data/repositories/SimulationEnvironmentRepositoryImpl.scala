package it.unibo.warverse.data.repositories

import it.unibo.warverse.data.data_sources.simulation_config.SimulationEnvironmentDataSource
import it.unibo.warverse.domain.model.common.Geometry
import it.unibo.warverse.domain.model.common.validation.Validation
import it.unibo.warverse.domain.repositories.SimulationEnvironmentRepository
import it.unibo.warverse.domain.model.common.Geometry.{
  MultiPolygon,
  Point2D,
  Polygon
}
import it.unibo.warverse.data.models.SimulationEnvironmentDtos.SimulationEnvironmentDto
import it.unibo.warverse.data.models.{ArmyDtos, GeometryDtos}
import it.unibo.warverse.data.models.WorldDtos.CountryDto
import it.unibo.warverse.data.models.ArmyDtos.ArmyUnitKind
import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.fight.Army
import it.unibo.warverse.domain.model.world.World
import it.unibo.warverse.domain.model.world.Relations.*
import monix.eval.Task

import java.io.File

trait DtoMapping:
  def mapEnvironmentDto(dto: SimulationEnvironmentDto): Environment
  def mapCountryDto(dto: CountryDto): World.Country
  def mapDtoToInterCountryRelations(
    configDto: SimulationEnvironmentDto
  ): InterCountryRelations
  def mapArmyDto(
    countryId: World.CountryId,
    dto: ArmyDtos.CountryArmy
  ): Seq[Army.ArmyUnit]
  def mapArmyUnitDto(
    countryId: World.CountryId,
    kind: ArmyDtos.ArmyUnitKind,
    unit: ArmyDtos.ArmyUnit
  ): Army.ArmyUnit
  def mapPoint2DDto(dto: GeometryDtos.Point2DDto): Geometry.Point2D

class SimulationEnvironmentRepositoryImpl(
  dataSource: SimulationEnvironmentDataSource
) extends SimulationEnvironmentRepository
    with DtoMapping:
  override def readSimulationEnvironment(
    file: File
  ): Task[Either[List[Validation.ValidationError], Environment]] =
    dataSource
      .readSimulationEnvironment(file)
      .map(result => result.validate().map(_ => mapEnvironmentDto(result)))

  override def mapEnvironmentDto(
    dto: SimulationEnvironmentDto
  ): Environment =
    Environment(
      countries = dto.countries.map(mapCountryDto),
      interCountryRelations = mapDtoToInterCountryRelations(dto)
    )

  override def mapCountryDto(dto: CountryDto): World.Country =
    World.Country(
      id = dto.id,
      name = dto.id,
      resources = dto.resources,
      citizens = dto.citizens,
      boundaries = MultiPolygon(Polygon(dto.boundaries.map(mapPoint2DDto))),
      armyUnits = mapArmyDto(dto.id, dto.army)
    )

  override def mapDtoToInterCountryRelations(
    configDto: SimulationEnvironmentDto
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

  override def mapArmyDto(
    countryId: World.CountryId,
    dto: ArmyDtos.CountryArmy
  ): Seq[Army.ArmyUnit] =
    dto.units.map(unit =>
      dto.unitKinds
        .find(_.id == unit.kind)
        .map(kind => mapArmyUnitDto(countryId, kind, unit))
        .get
    )

  override def mapArmyUnitDto(
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

  override def mapPoint2DDto(dto: GeometryDtos.Point2DDto): Geometry.Point2D =
    Geometry.Point2D(
      x = dto.x,
      y = dto.y
    )
