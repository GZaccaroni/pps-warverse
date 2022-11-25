package it.unibo.warverse.presentation.common

import java.net.URL
import java.awt.BasicStroke
import it.unibo.warverse.domain.model.common.Geometry.Point2D
import it.unibo.warverse.domain.model.common.Geometry.Polygon2D
import it.unibo.warverse.domain.model.world.World.Country
import it.unibo.warverse.domain.model.world.World.CountryId
import it.unibo.warverse.domain.model.world.World.Citizen
import it.unibo.warverse.domain.model.common.Geometry
import java.awt.Dimension
import it.unibo.warverse.domain.model.fight.Army.*

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
  val testCountries: List[Country] = List(
    Country(
      idCountry1,
      "War",
      1,
      List(
        PrecisionArmyUnit(
          idCountry1,
          "Soldier",
          Point2D(250, 150),
          0.5,
          200,
          5,
          200,
          15
        ),
        PrecisionArmyUnit(
          idCountry1,
          "Soldier",
          Point2D(250, 250),
          0.5,
          200,
          5,
          200,
          15
        ),
        AreaArmyUnit(
          idCountry1,
          "Mortar",
          Point2D(250, 200),
          0.5,
          30,
          500,
          10,
          20,
          30
        ),
        AreaArmyUnit(
          idCountry1,
          "Mortar",
          Point2D(250, 300),
          0.5,
          30,
          500,
          10,
          20,
          30
        )
      ),
      0.0,
      Polygon2D(
        List(
          Point2D(150, 150),
          Point2D(250, 100),
          Point2D(325, 125),
          Point2D(375, 225),
          Point2D(450, 250),
          Point2D(275, 375),
          Point2D(100, 300)
        )
      )
    ),
    Country(
      idCountry2,
      "Warverse",
      1,
      List.empty,
      0.0,
      Polygon2D(
        List(
          Point2D(550, 550),
          Point2D(650, 500),
          Point2D(925, 600)
        )
      )
    ),
    Country(
      idCountry3,
      "AnotherTest",
      1,
      List.empty,
      0.0,
      Polygon2D(
        List(
          Point2D(1050, 150),
          Point2D(1000, 50),
          Point2D(1000, 150),
          Point2D(1050, 250)
        )
      )
    )
  )
