package it.unibo.warverse.model.world

import it.unibo.warverse.model.world.World.Country

import scala.::

object InterstateRelations:

  enum RelationStatus:
    case ALLIANCE, NEUTRAL, WAR

  type Relation = (Country, Country, RelationStatus)

  trait InterstateRelations:

    def relations: List[Relation]

    def addRelation(relation: Relation): InterstateRelations

    def removeRelation(relation: Relation): InterstateRelations

    def getAllies(country: Country): Iterable[Country]

    def getWars(country: Country): Iterable[Country]

  case class InterstateRelationsImpl(
    override val relations: List[Relation]
  ) extends InterstateRelations:

    override def addRelation(
      relation: Relation
    ): InterstateRelations =
      InterstateRelationsImpl(relations :+ relation)

    override def removeRelation(
      relation: Relation
    ): InterstateRelations = InterstateRelationsImpl(
      relations.filterNot(_ == relation)
    )

    override def getAllies(country: Country): Iterable[Country] =
      getRelatedCountry(country, RelationStatus.ALLIANCE)

    override def getWars(country: Country): Iterable[Country] =
      getRelatedCountry(country, RelationStatus.WAR)

    private def getRelatedCountry(country: Country, status: RelationStatus) =
      relations.collect({
        case (`country`, otherCountry, `status`) =>
          otherCountry
        case (otherCountry, `country`, `status`) =>
          otherCountry
      })
