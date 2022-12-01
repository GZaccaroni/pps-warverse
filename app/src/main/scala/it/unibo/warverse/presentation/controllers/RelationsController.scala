package it.unibo.warverse.presentation.controllers

import it.unibo.warverse.domain.model.world.Relations.*
import it.unibo.warverse.domain.model.world.World.Country

class RelationsController:

  def updateRelations(
    relations: InterstateRelations,
    countries: List[Country]
  ): InterstateRelations =
    var newRel: InterstateRelations = relations
    countries.foreach(country =>
      val enemies = relations.getEnemies(country.id)
      val allied = relations.getAllies(country.id)
      enemies.map(enemy =>
        allied.foreach(ally =>
          if relations.getStatus(enemy, ally).isEmpty then
            newRel = newRel.withRelation((enemy, ally), RelationStatus.WAR)
        )
      )
    )
    newRel

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
    countries.filter(country => relations.getEnemies(country.id).size > 0)

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
