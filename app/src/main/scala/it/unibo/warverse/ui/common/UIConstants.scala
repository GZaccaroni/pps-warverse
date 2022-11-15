package it.unibo.warverse.ui.common

import java.net.URL
import java.awt.BasicStroke
import it.unibo.warverse.model.common.Geometry.Point2D
import it.unibo.warverse.model.common.Geometry.Polygon2D
import it.unibo.warverse.model.world.World.Country
import it.unibo.warverse.model.world.World.Citizen
import it.unibo.warverse.model.common.Geometry
import java.awt.Dimension
import it.unibo.warverse.model.fight.Army.*

object UIConstants:
  enum Resources(val name: String):
    case MainMenuBackground extends Resources("menuBackground.png")
    case HelpMenuBackground extends Resources("menuHelp.png")
    case Test extends Resources("test.png")

    def url: URL = ClassLoader.getSystemResource(name)

  val borderMap = new Dimension(1400, 700)

  val borderRegion = new BasicStroke(
    4.0f,
    BasicStroke.CAP_BUTT,
    BasicStroke.JOIN_MITER,
    1.0f,
    Array(1.0f),
    0.1f
  )

  val testCountries: Array[Country] = Array(
    Country(
      "War",
      List(Citizen(Geometry.Point2D(150, 150))),
      List(
        PrecisionArmyUnit(
          "Soldier",
          0.5,
          200,
          5,
          200,
          15,
          Point2D(250, 150)
        ),
        PrecisionArmyUnit(
          "Soldier",
          0.5,
          200,
          5,
          200,
          15,
          Point2D(250, 250)
        ),
        AreaArmyUnit(
          "Mortar",
          0.5,
          30,
          500,
          10,
          20,
          Point2D(250, 200),
          30
        ),
        AreaArmyUnit(
          "Mortar",
          0.5,
          30,
          500,
          10,
          20,
          Point2D(250, 300),
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
      "Warverse",
      List(Citizen(Geometry.Point2D(550, 550))),
      List(),
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
      "AnotherTest",
      List(Citizen(Geometry.Point2D(550, 550))),
      List(),
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
