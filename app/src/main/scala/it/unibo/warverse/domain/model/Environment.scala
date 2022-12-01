package it.unibo.warverse.domain.model

import it.unibo.warverse.domain.model.world.World.Country
import it.unibo.warverse.domain.model.fight.Army.ArmyUnit

trait Environment:
  def countries: List[Country]
  def day_=(day: Integer): Unit
  def day: Integer
  def setCountries(countries: List[Country]): Environment
  def getCountries: List[Country]
  def nextDay(): Environment
  def updateCountries(newCountries: List[Country]): Environment

object Environment:
  def apply(countries: List[Country], day: Integer): Environment =
    EnvironmentImpl(countries, day)

  def apply(): Environment =
    EnvironmentImpl(List(), 0)

  private case class EnvironmentImpl(
    override val countries: List[Country],
    override var day: Integer
  ) extends Environment:

    override def updateCountries(newCountries: List[Country]): Environment =
      this.copy(countries = newCountries)

    override def setCountries(countries: List[Country]): Environment =
      Environment(countries, this.day)

    override def getCountries: List[Country] = this.countries
    def nextDay(): Environment =
      Environment(this.countries, this.day + 1)
