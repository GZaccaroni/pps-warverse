package it.unibo.warverse.presentation.controllers

import it.unibo.warverse.domain.model.world.Relations.*
import it.unibo.warverse.domain.model.world.World.Country

class RelationsController:

  def updateRelations(
    relations: InterstateRelations,
    countries: Seq[Country]
  ): InterstateRelations =
    var newRel: InterstateRelations = relations
    countries.foreach(country =>
      val enemies = relations.countryEnemies(country.id)
      val allied = relations.countryAllies(country.id)
      enemies.map(enemy =>
        allied.foreach(ally =>
          if newRel.getStatus(enemy, ally).isEmpty then
            newRel = newRel.withRelation((enemy, ally), RelationStatus.WAR)
        )
      )
    )
    newRel

  def noWars(
    relations: InterstateRelations,
    countries: Seq[Country]
  ): Boolean =
    var check = true
    countries.foreach(country =>
      if relations.countryEnemies(country.id).nonEmpty then check = false
    )
    check

  def getWars(
    relations: InterstateRelations,
    countries: Seq[Country]
  ): Seq[Country] =
    countries.filter(country => relations.countryEnemies(country.id).nonEmpty)

  def removeLostStateRelation(
    country: Country,
    relations: InterstateRelations
  ): InterstateRelations =
    var result: InterstateRelations = relations
    val enemies = relations.countryEnemies(country.id)
    val allied = relations.countryAllies(country.id)
    allied.foreach(ally =>
      result =
        result.withoutRelation((country.id, ally), RelationStatus.ALLIANCE)
    )
    enemies.foreach(enemy =>
      result = result.withoutRelation((country.id, enemy), RelationStatus.WAR)
    )
    result
