package it.unibo.warverse.ui.common

import java.net.URL
import java.awt.BasicStroke
import it.unibo.warverse.model.common.Geometry.Point2D
import it.unibo.warverse.model.common.Geometry.Polygon2D
import it.unibo.warverse.model.world.World.Country
import it.unibo.warverse.model.world.World.Citizen
import it.unibo.warverse.model.common.Geometry

object UIConstants:
  enum Resources(val name: String):
    case MainMenuBackground extends Resources("menuBackground.png")
    case HelpMenuBackground extends Resources("menuHelp.png")

    def url: URL = ClassLoader.getSystemResource(name)

  val borderRegion = new BasicStroke(
    4.0f,
    BasicStroke.CAP_BUTT,
    BasicStroke.JOIN_MITER,
    1.0f,
    Array(1.0f),
    0.1f
  )

  val testCountries = Array(
    Country(
      "War",
      List(Citizen(Geometry.Point2D(150, 150))),
      List(),
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
          Point2D(1200, 150),
          Point2D(1000, 50),
          Point2D(1000, 150),
          Point2D(1200, 250),
        )
      )
    )
  )
