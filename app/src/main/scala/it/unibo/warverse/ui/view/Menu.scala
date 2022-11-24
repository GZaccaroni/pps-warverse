package it.unibo.warverse.ui.view

import it.unibo.warverse.ui.common.UIConstants

class Menu(mainFrame: MainFrame) extends MenuActions(mainFrame):
  backgroundImageUrl = Option(
    UIConstants.Resources.MainMenuBackground.url
  )
