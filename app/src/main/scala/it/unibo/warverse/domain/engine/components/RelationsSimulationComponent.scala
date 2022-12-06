package it.unibo.warverse.domain.engine.components

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.world.Relations.*
import it.unibo.warverse.domain.model.world.World.Country

class RelationsSimulationComponent extends SimulationComponent:
  override def run(environment: Environment): Environment =
    var newRel = environment.interstateRelations
    environment.countries.foreach(country =>
      val enemies = environment.interstateRelations.countryEnemies(country.id)
      val allied = environment.interstateRelations.countryAllies(country.id)
      enemies.map(enemy =>
        allied.foreach(ally =>
          if newRel.getStatus(enemy, ally).isEmpty then
            newRel = newRel.withRelation((enemy, ally), RelationStatus.WAR)
        )
      )
    )
    environment.copiedWith(interstateRelations = newRel)
