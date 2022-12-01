package it.unibo.warverse.presentation.controllers

import it.unibo.warverse.domain.model.world.GameStats
import it.unibo.warverse.domain.model.world.World.Country
import it.unibo.warverse.domain.model.world.World

class GameStatsController:
  private val gameStats: GameStats = GameStats()

  def updateStatsEvents(
    winnerCountry: World.CountryId,
    looserCountry: Country,
    day: Int
  ): Unit =
    this.gameStats.updateEventList(winnerCountry, looserCountry, day)
