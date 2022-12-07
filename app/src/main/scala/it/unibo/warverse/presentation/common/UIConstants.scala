package it.unibo.warverse.presentation.common

import java.net.URL
import java.awt.BasicStroke
import it.unibo.warverse.domain.model.common.Geometry.Point2D
import it.unibo.warverse.domain.model.common.Geometry.Polygon2D
import it.unibo.warverse.domain.model.world.World.Country
import it.unibo.warverse.domain.model.world.World.CountryId
import it.unibo.warverse.domain.model.common.Geometry
import java.awt.Dimension
import it.unibo.warverse.domain.model.fight.Army.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.json4s.jackson.Json

object UIConstants:
  enum Resources(val name: String):
    case MainMenuBackground extends Resources("menuBackground.png")
    case HelpMenuBackground extends Resources("menuHelp.png")
    case Test extends Resources("test.png")

    def url: URL = ClassLoader.getSystemResource(name)

  val borderMap: Dimension = Dimension(1400, 700)

  val borderRegion: BasicStroke = BasicStroke(
    4.0f,
    BasicStroke.CAP_BUTT,
    BasicStroke.JOIN_MITER,
    1.0f,
    Array(1.0f),
    0.1f
  )
  private val idCountry1: CountryId = "Country_1"
  private val idCountry2: CountryId = "Country_2"
  private val idCountry3: CountryId = "Country_3"

  val structure =
    """{"countries": [{"id": "COUNTRY_NAME","citizens": 0,"army": {"unit_kinds": [],"units": []},"resources": 0.00,"boundaries": [],"relations": {"allies": [],"enemies": []}}]}"""
  val country =
    """{"id" : "COUNTRY_NAME","citizens" : 0,"army" : {"unit_kinds" : [ ],"units" : [ ]},"resources" : 0.0,"boundaries" : [ ],"relations" : {"allies" : [ ],"enemies" : [ ] }}"""
  val relations =
    """{"relations": {"allies": ["Ally1","Ally2","Ally3"],"enemies": ["Enemy1","Enemy2","Enemy3"]}}"""
  val unit_kinds =
    """ {"kind": "soldier","position": {"x": 550,"y": 150}}"""
  val unit =
    """ {  "id": "soldier","name": "Soldier","attack_type": "precision","hit_chance": 50,"hit_range": 200.0,"maximum_hits": 5,"daily_resources_usage": 2.0,"speed": 15.0}"""
  val boundaries = """{ "x": 550, "y": 150}"""
