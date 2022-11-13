package it.unibo.warverse.ui.common

import java.net.URL

object UIConstants:
  enum Resources(val name: String):
    case MainMenuBackground extends Resources("menuBackground.png")
    case HelpMenuBackground extends Resources("menuHelp.png")

    def url: URL = ClassLoader.getSystemResource(name)
