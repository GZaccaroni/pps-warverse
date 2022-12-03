package it.unibo.warverse.domain.model

import it.unibo.warverse.domain.model.world.World.Country
import it.unibo.warverse.domain.model.fight.Army.ArmyUnit
import it.unibo.warverse.domain.model.world.Relations.InterstateRelations

trait Environment:
  def countries: Seq[Country]
  def interstateRelations: InterstateRelations
  def day: Int
  def copiedWith(
    countries: Seq[Country] = countries,
    interstateRelations: InterstateRelations = interstateRelations,
    day: Int = day
  ): Environment =
    Environment(countries, interstateRelations, day)

object Environment:
  def apply(
    countries: Seq[Country],
    interstateRelations: InterstateRelations,
    day: Int
  ): Environment =
    EnvironmentImpl(countries, interstateRelations, day)

  def initial(countries: Seq[Country]): Environment =
    EnvironmentImpl(countries, InterstateRelations(Seq.empty), 0)

  private case class EnvironmentImpl(
    override val countries: Seq[Country],
    override val interstateRelations: InterstateRelations,
    override val day: Int
  ) extends Environment
