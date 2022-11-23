package it.unibo.warverse.model.world

import it.unibo.warverse.model.world.World.Country

import scala.::
import javax.swing.JOptionPane

object Relations:

  enum RelationStatus:
    case ALLIANCE, NEUTRAL, WAR

  type Relation = ((Country, Country), RelationStatus)

  trait InterstateRelations:

    def relations: List[Relation]

    def addRelation(relation: Relation): InterstateRelations

    def removeRelation(relation: Relation): InterstateRelations

    def getAllies(country: Country): Iterable[Country]

    def getWars(country: Country): Iterable[Country]

  object InterstateRelations:
    def apply(relations: List[Relation]): InterstateRelations =
      InterstateRelationsImpl(relations)

    private case class InterstateRelationsImpl(
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
          case ((`country`, otherCountry), `status`) =>
            otherCountry
          case ((otherCountry, `country`), `status`) =>
            otherCountry
        })

      private def checkDuplicatedRelations(): Unit =
        val couples =
          for couple <- relations.map(_._1)
          yield couple

        if couples
            .flatMap(c => List(c, c.swap))
            .distinct
            .length != couples.length * 2
        then
          JOptionPane.showMessageDialog(
            null,
            "Invalid State Relation in json file."
          )
          throw new IllegalStateException("Invalid Relation")

      private def dropDuplicates: InterstateRelations =
        val duplicates =
          for
            (c, r) <- relations
            c1 = c.swap
            if relations.contains((c1, r))
          yield (c1, r)
        InterstateRelationsImpl(relations.distinct.diff(duplicates))

      private def checkIllegalRelation(): Unit =
        val illegalRelation =
          for
            (c, r) <- relations
            c1 = c.swap
            if relations.exists(x => (x._1 == c || x._1 == c1) && (x._2 != r))
          yield (c, r)
        if illegalRelation.nonEmpty then
          throw new IllegalStateException("Invalid Relations")
