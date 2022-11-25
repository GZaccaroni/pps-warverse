package it.unibo.warverse.presentation.view

import it.unibo.warverse.presentation.common.UIConstants

class MenuHelp(mainFrame: MainFrame) extends MenuActions(mainFrame):
  backgroundImageUrl = Option(
    UIConstants.Resources.HelpMenuBackground.url
  )
