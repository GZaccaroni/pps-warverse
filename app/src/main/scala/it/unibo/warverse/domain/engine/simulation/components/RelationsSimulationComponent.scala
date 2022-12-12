package it.unibo.warverse.domain.engine.simulation.components

import it.unibo.warverse.domain.model.Environment
import it.unibo.warverse.domain.model.world.Relations.*
import it.unibo.warverse.domain.model.world.World.Country
import it.unibo.warverse.domain.model.common.Listen.*
import it.unibo.warverse.domain.model.fight.SimulationEvent
import monix.eval.Task

/** Updates country enemies accordingly to ally enemies
  */
class RelationsSimulationComponent
    extends SimpleListenable[SimulationEvent]
    with SimulationComponent:
  override def run(environment: Environment): Task[Environment] =
    Task {
      val newRelations = environment.countries
        .foldLeft(environment.interCountryRelations) { (relations, country) =>
          val enemies = relations.countryEnemies(country.id)
          val allies = relations.countryAllies(country.id)
          val enemiesAlliesPairs =
            enemies.flatMap(enemy => allies.map(ally => (enemy, ally)))

          enemiesAlliesPairs.foldLeft(relations) { (relations, pair) =>
            if relations.getStatus(pair._1, pair._2).isEmpty then
              relations.withRelation((pair._1, pair._2), RelationStatus.WAR)
            else relations
          }
        }
      environment.copiedWith(interCountryRelations = newRelations)
    }
