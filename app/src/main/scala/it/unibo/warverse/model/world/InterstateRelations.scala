package it.unibo.warverse.model.world

import it.unibo.warverse.model.world.World.Country

import java.util

object InterstateRelations:

  enum RelationStatus:
    case ALLIANCE, NEUTRAL, WAR

  trait InterstateRelations:
    type Relation

    def relations: List[Relation]

    def addRelation(relation: Relation): InterstateRelations

    def removeRelation(relation: Relation): InterstateRelations

    def getAllies(country: Country): Iterable[Country]

    def getWars(country: Country): Iterable[Country]
