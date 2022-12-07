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

private[presentation] object UIConstants:
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
