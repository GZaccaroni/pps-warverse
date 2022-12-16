package it.unibo.warverse.data.samples

import com.fasterxml.jackson.databind.ObjectMapper
import org.json4s.{DefaultFormats, Formats}
import it.unibo.warverse.data.data_sources.simulation_config.Serializers.*
import it.unibo.warverse.data.models.GeometryDtos.Point2DDto
import it.unibo.warverse.data.models.{
  ArmyDtos,
  GeometryDtos,
  SimulationConfigDtos,
  WorldDtos
}
import org.json4s.jackson.Serialization

trait EncodedSamples:

  def complete: String
  def country: String
  def countryRelations: String
  def armyUnit: String
  def armyUnitKind: String
  def countryBoundaries: String

object EncodedSamples:
  def apply(): EncodedSamples = EncodedSamplesJsonImpl()

  private class EncodedSamplesJsonImpl extends EncodedSamples:
    implicit val formats: Formats =
      DefaultFormats + UnitAttackTypeSerializer()

    override def complete: String =
      Serialization.writePretty(
        SimulationConfigDtos.SimulationConfigDto(
          countries = sampleCountry :: Nil
        )
      )

    override def country: String =
      Serialization.writePretty(sampleCountry)

    private val sampleCountry =
      WorldDtos.CountryDto(
        id = "COUNTRY_ID",
        citizens = 500,
        army = ArmyDtos.CountryArmy(
          unitKinds = List.empty,
          units = List.empty
        ),
        resources = 50,
        boundaries = List.empty,
        relations = WorldDtos.CountryRelationsDto()
      )

    override def countryRelations: String =
      Serialization.writePretty(
        sampleCountryRelations
      )

    private val sampleCountryRelations =
      WorldDtos.CountryRelationsDto(
        allies = "Ally1" :: "Ally2" :: "Ally3" :: Nil,
        enemies = "Enemy1" :: "Enemy2" :: "Enemy3" :: Nil
      )

    override def armyUnit: String =
      Serialization.writePretty(sampleArmyUnit)

    private val sampleArmyUnit =
      ArmyDtos.ArmyUnit(
        kind = "soldier",
        position = Point2DDto(0, 5)
      )

    override def armyUnitKind: String =
      Serialization.writePretty(
        sampleArmyUnitKind
      )

    private val sampleArmyUnitKind =
      ArmyDtos.ArmyUnitKind.PrecisionArmyUnitKind(
        id = "soldier",
        name = "Soldier",
        hitChance = 50,
        hitRange = 100,
        maximumHits = 5,
        dailyResourcesUsage = 2.0,
        speed = 5.0
      )

    override def countryBoundaries: String =
      Serialization.writePretty(sampleCountryBoundaries)

    private val sampleCountryBoundaries =
      List(
        GeometryDtos.Point2DDto(0, 0),
        GeometryDtos.Point2DDto(10, 0),
        GeometryDtos.Point2DDto(10, 10),
        GeometryDtos.Point2DDto(0, 10)
      )
