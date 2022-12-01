package it.unibo.warverse.presentation.controllers

import it.unibo.warverse.domain.model.world.Relations.*
import it.unibo.warverse.domain.model.world.World.Country

class RelationsController:

  def updateRelations(
    relations: InterstateRelations,
    countries: List[Country]
  ): Unit =
    countries.foreach(country =>
      val enemies = relations.getEnemies(country.id)
      val allied = relations.getAllies(country.id)
      enemies.map(enemy =>
        allied.foreach(ally =>
          if relations.getStatus(enemy, ally) == RelationStatus.NEUTRAL then
            relations.setWar(enemy, ally)
        )
      )
    )

  def noWars(
    relations: InterstateRelations,
    countries: List[Country]
  ): Boolean =
    var check = true
    countries.foreach(country =>
      if relations.getEnemies(country.id).size > 0 then check = false
    )
    check
