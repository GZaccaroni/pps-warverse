package it.unibo.warverse.presentation.model

object MenuItems:
  trait MenuItem:
    def label: String
  enum MainMenuItem(val label: String) extends MenuItem:
    case StartGame extends MainMenuItem(label = "Start Game")
    case Help extends MainMenuItem(label = "Help")
    case Exit extends MainMenuItem(label = "Exit")
