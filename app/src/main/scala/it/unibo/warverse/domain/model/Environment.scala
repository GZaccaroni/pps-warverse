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
  def interCountryRelations: InterCountryRelations

  /** Returns the current day in the environment
    *
    * @return
    *   current day
    */
  def day: Int

  /** Creates a copy of environment with the passed parameters updated
    * @param countries
    *   new countries
    * @param interCountryRelations
    *   new interstate releations
    * @param day
    *   new day
    * @return
    *   a copy of environment
    */
  def copiedWith(
    countries: Seq[Country] = countries,
    interCountryRelations: InterCountryRelations = interCountryRelations,
    day: Int = day
  ): Environment =
    Environment(countries, interCountryRelations, day)

  /** Creates a new environment replacing the country with the same CountryId of
    * `updatedCountry`
    * @param updatedCountry
    *   the new country that be part of
    * @return
    *   the updated environment
    */
  def replacingCountry(updatedCountry: Country): Environment =
    copiedWith(
      updatedCountry +: countries.filterNot(_.id == updatedCountry.id)
    )

object Environment:
  def apply(
    countries: Seq[Country],
    interCountryRelations: InterCountryRelations,
    day: Int = 0
  ): Environment =
    EnvironmentImpl(countries, interCountryRelations, day)

  private case class EnvironmentImpl(
    override val countries: Seq[Country],
    override val interCountryRelations: InterCountryRelations,
    override val day: Int
  ) extends Environment
