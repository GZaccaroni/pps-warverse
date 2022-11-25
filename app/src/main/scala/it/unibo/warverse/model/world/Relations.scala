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

    def withRelation(relation: Relation): InterstateRelations

    def withoutRelation(relation: Relation): InterstateRelations

    def getAllies(country: Country): Iterable[Country]

    def getEnemies(country: Country): Iterable[Country]

  object InterstateRelations:
    def apply(relations: List[Relation]): InterstateRelations =
      InterstateRelationsImpl(relations).dropDuplicates

    private case class InterstateRelationsImpl(
      override val relations: List[Relation]
    ) extends InterstateRelations:

      checkIllegalRelation()

      override def withRelation(
        relation: Relation
      ): InterstateRelations =
        InterstateRelationsImpl(relations :+ relation)

      override def withoutRelation(
        relation: Relation
      ): InterstateRelations = InterstateRelationsImpl(
        relations.filterNot(_ == relation)
      )

      override def getAllies(country: Country): Iterable[Country] =
        getRelatedCountry(country, RelationStatus.ALLIANCE)

      override def getEnemies(country: Country): Iterable[Country] =
        getRelatedCountry(country, RelationStatus.WAR)

      private def getRelatedCountry(country: Country, status: RelationStatus) =
        relations.collect({
          case ((`country`, otherCountry), `status`) =>
            otherCountry
          case ((otherCountry, `country`), `status`) =>
            otherCountry
        })

      def dropDuplicates: InterstateRelations =
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
          throw IllegalStateException("Invalid Relations")
