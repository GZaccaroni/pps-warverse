package it.unibo.warverse.presentation.common

import java.net.URL
import java.awt.{BasicStroke, Dimension}

private[presentation] object UIConstants:
  enum Resources(val name: String):
    case MainMenuBackground extends Resources("menuBackground.png")
    case HelpMenuBackground extends Resources("menuHelp.png")

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

  val helpDescription: String =
    "<html>" +
      "<p style=\"line-height: 2; color:white;\">Welcome to WarVerse ⚔️ your custom war Simulator💻! " +
      "<br/>Create a world🌍 with your custom countries with their citizen👤, resources💰 and armies🚀 and set all relationship between them and check how the war evolve! " +
      "<br/>In order to do that, this simulator need a valid JSON file to configure the simulation environment." +
      "<br/>To do it, you can check and use the tool on the right and see how it look's like." +
      "<br/>Once you have created your JSON file, you can have fun! " +
      "<b style=\"margin-left: 5px;font-size:10px; color:white;\">The WarVerse Team 🤟🌍</b></p></html>"
