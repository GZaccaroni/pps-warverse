package it.unibo.warverse.model

import it.unibo.warverse.model.world.World.Country
import it.unibo.warverse.model.fight.Army.ArmyUnit

trait Enviroment:
  def countries: List[Country]
  def army: List[ArmyUnit]
  def day: Integer

class EnviromentImpl:
  println()
