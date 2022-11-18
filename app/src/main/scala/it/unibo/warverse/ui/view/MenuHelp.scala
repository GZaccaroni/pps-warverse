package it.unibo.warverse.ui.view

import it.unibo.warverse.ui.common.UIConstants

class MenuHelp(mainFrame: MainFrame) extends MenuActions(mainFrame):
  backgroundImageUrl = Option(
    UIConstants.Resources.HelpMenuBackground.url
  )
