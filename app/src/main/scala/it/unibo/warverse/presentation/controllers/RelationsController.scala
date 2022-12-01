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
          if relations.getStatus(enemy, ally).nonEmpty then
            if relations.getStatus(enemy, ally).last == RelationStatus.NEUTRAL
            then relations.setWar(enemy, ally)
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

  def getWars(
    relations: InterstateRelations,
    countries: List[Country]
  ): List[Country] =
    countries.map(country =>
      if relations.getEnemies(country.id).size > 0
      then country
      else null
    )

  def removeLostStateRelation(
    country: Country,
    relations: InterstateRelations
  ): InterstateRelations =
    var result: InterstateRelations = relations
    val enemies = relations.getEnemies(country.id)
    val allied = relations.getAllies(country.id)
    allied.foreach(ally =>
      result =
        result.withoutRelation((country.id, ally), RelationStatus.ALLIANCE)
    )
    enemies.foreach(enemy =>
      result = result.withoutRelation((country.id, enemy), RelationStatus.WAR)
    )
    result
