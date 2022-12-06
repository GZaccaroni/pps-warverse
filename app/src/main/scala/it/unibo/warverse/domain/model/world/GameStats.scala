package it.unibo.warverse.domain.model.world

import it.unibo.warverse.domain.model.world.World.Country

trait GameStats:
  def getResult(): List[SingleEvent]
  def eventList: List[SingleEvent]
  def updateEventList(
    country1: World.CountryId,
    country2: World.CountryId,
    day: Int
  ): GameStats

type SingleEvent = (World.CountryId, World.CountryId, Int)

object GameStats:
  def apply(): GameStats = GameStatsImpl(List())
  def apply(eventList: List[SingleEvent]): GameStats = GameStatsImpl(eventList)

  private case class GameStatsImpl(
    override val eventList: List[SingleEvent]
  ) extends GameStats:
    override def getResult(): List[SingleEvent] = this.eventList
    override def updateEventList(
      country1: World.CountryId,
      country2: World.CountryId,
      day: Int
    ): GameStats =
      this.copy(eventList = eventList ++ List((country1, country2, day)))
