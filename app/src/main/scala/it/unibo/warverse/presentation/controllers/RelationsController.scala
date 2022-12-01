package it.unibo.warverse.presentation.controllers

import it.unibo.warverse.domain.model.world.Relations.*
import it.unibo.warverse.domain.model.world.World.Country

class RelationsController:

  def updateRelations(
    relations: InterstateRelations,
    countries: Seq[Country]
  ): Unit =
    countries.foreach(country =>
      val enemies = relations.countryEnemies(country.id)
      val allied = relations.countryAllies(country.id)
      enemies.map(enemy =>
        allied.foreach(ally =>
          if relations.getStatus(enemy, ally) == RelationStatus.NEUTRAL then
            relations.setWar(enemy, ally)
        )
      )
    )

  def noWars(
    relations: InterstateRelations,
    countries: Seq[Country]
  ): Boolean =
    var check = true
    countries.foreach(country =>
      if relations.countryEnemies(country.id).nonEmpty then check = false
    )
    check
