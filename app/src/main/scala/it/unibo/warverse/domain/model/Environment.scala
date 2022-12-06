package it.unibo.warverse.domain.model

import it.unibo.warverse.domain.model.world.World.Country
import it.unibo.warverse.domain.model.fight.Army.ArmyUnit
import it.unibo.warverse.domain.model.world.Relations.InterstateRelations

import scala.::

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
  def replacingCountry(newCountry: Country): Environment =
    copiedWith(
      newCountry :: countries.filter(_.id != newCountry.id).toList
    )

object Environment:
  def apply(
    countries: Seq[Country],
    interstateRelations: InterstateRelations,
    day: Int = 0
  ): Environment =
    EnvironmentImpl(countries, interstateRelations, day)

  private case class EnvironmentImpl(
    override val countries: Seq[Country],
    override val interstateRelations: InterstateRelations,
    override val day: Int
  ) extends Environment
