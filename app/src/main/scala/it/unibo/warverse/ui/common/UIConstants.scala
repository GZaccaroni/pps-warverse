package it.unibo.warverse.ui.common

import java.net.URL
import java.awt.BasicStroke

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
