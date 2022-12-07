package it.unibo.warverse.domain.model.world

import it.unibo.warverse.domain.model.world.World.Country

/** Representation of the current stats of the simulation
  */
trait SimulationStats:
  /** Returns a list of all the events
    * @return
    *   a list of {@link SingleEvent}
    */
  def eventList: List[SingleEvent]

  /** Adds an event to events list and returns an updated {@link
    * SimulationStats}
    *
    * @param country1
    *   First country affected by event
    * @param country2
    *   Second country affected by event
    * @param day
    *   Day of the event
    * @return
    *   updated {@link SimulationStats}
    */
  def updateEventList(
    country1: World.CountryId,
    country2: World.CountryId,
    day: Int
  ): SimulationStats

type SingleEvent = (World.CountryId, World.CountryId, Int)

object SimulationStats:
  /** Factory that builds an instance of {@link SimulationStats} with no events
    * @return
    *   an instance of {@link SimulationStats}
    */
  def apply(): SimulationStats = SimulationStatsImpl(List())

  /** Factory that builds an instance of {@link SimulationStats} with the given
    * list events
    * @param eventList
    *   a list of events to insert in the {@link SimulationStats}
    * @return
    *   an instance of {@link SimulationStats}
    */
  def apply(eventList: List[SingleEvent]): SimulationStats =
    SimulationStatsImpl(eventList)

  private case class SimulationStatsImpl(
    override val eventList: List[SingleEvent]
  ) extends SimulationStats:
    override def updateEventList(
      country1: World.CountryId,
      country2: World.CountryId,
      day: Int
    ): SimulationStats =
      this.copy(eventList = eventList ++ List((country1, country2, day)))
