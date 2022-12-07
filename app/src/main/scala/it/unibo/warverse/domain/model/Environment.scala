package it.unibo.warverse.domain.model

import it.unibo.warverse.domain.model.world.World.Country
import it.unibo.warverse.domain.model.fight.Army.ArmyUnit
import it.unibo.warverse.domain.model.world.Relations.InterCountryRelations

import scala.::

/** Environment of the simulation
  */
trait Environment:
  /** Returns the countries
    * @return
    *   a sequence of countries
    */
  def countries: Seq[Country]

  /** Returns the relations between countries
    *
    * @return
    *   relations between countries
    */
  def interstateRelations: InterCountryRelations

  /** Returns the current day in the environment
    *
    * @return
    *   current day
    */
  def day: Int

  /** Creates a copy of environment with the passed parameters updated
    * @param countries
    *   new countries
    * @param interstateRelations
    *   new interstate releations
    * @param day
    *   new day
    * @return
    *   a copy of environment
    */
  def copiedWith(
    countries: Seq[Country] = countries,
    interstateRelations: InterCountryRelations = interstateRelations,
    day: Int = day
  ): Environment =
    Environment(countries, interstateRelations, day)

  /** A new environment with `newCountry` replaced
    * @param newCountry
    *   the country to be replaced
    * @return
    *   the updated environment
    */
  def replacingCountry(newCountry: Country): Environment =
    copiedWith(
      newCountry :: countries.filter(_.id != newCountry.id).toList
    )

object Environment:
  def apply(
    countries: Seq[Country],
    interstateRelations: InterCountryRelations,
    day: Int = 0
  ): Environment =
    EnvironmentImpl(countries, interstateRelations, day)

  private case class EnvironmentImpl(
    override val countries: Seq[Country],
    override val interstateRelations: InterCountryRelations,
    override val day: Int
  ) extends Environment
