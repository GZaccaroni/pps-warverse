package it.unibo.warverse.domain.model

import it.unibo.warverse.domain.model.world.World.Country
import it.unibo.warverse.domain.model.fight.Army.ArmyUnit

trait Environment:
  def countries: Seq[Country]
  def day: Int
  def copiedWith(
    countries: Seq[Country] = countries,
    day: Int = day
  ): Environment =
    Environment(countries, day)

object Environment:
  def apply(countries: Seq[Country], day: Int): Environment =
    EnvironmentImpl(countries, day)

  def initial(countries: Seq[Country]): Environment =
    EnvironmentImpl(countries, 0)

  private case class EnvironmentImpl(
    override val countries: Seq[Country],
    override val day: Int
  ) extends Environment
