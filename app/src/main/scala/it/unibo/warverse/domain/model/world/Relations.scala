package it.unibo.warverse.domain.model.world

import it.unibo.warverse.domain.model.world.World.Country

import scala.::
import javax.swing.JOptionPane

object Relations:

  enum RelationStatus:
    case ALLIANCE, NEUTRAL, WAR

  type InterstateRelation = ((World.CountryId, World.CountryId), RelationStatus)

  trait InterstateRelations:

    def relations: Seq[InterstateRelation]

    def withRelation(relation: InterstateRelation): InterstateRelations

    def withoutRelation(relation: InterstateRelation): InterstateRelations

    def getAllies(country: World.CountryId): Iterable[World.CountryId]

    def getEnemies(country: World.CountryId): Iterable[World.CountryId]

  object InterstateRelations:
    def apply(relations: Seq[InterstateRelation]): InterstateRelations =
      InterstateRelationsImpl(relations).dropDuplicates

    private case class InterstateRelationsImpl(
      override val relations: Seq[InterstateRelation]
    ) extends InterstateRelations:

      checkIllegalRelation()

      override def withRelation(
        relation: InterstateRelation
      ): InterstateRelations =
        InterstateRelationsImpl(relations :+ relation)

      override def withoutRelation(
        relation: InterstateRelation
      ): InterstateRelations = InterstateRelationsImpl(
        relations.filterNot(_ == relation)
      )

      override def getAllies(
        country: World.CountryId
      ): Iterable[World.CountryId] =
        getRelatedCountry(country, RelationStatus.ALLIANCE)

      override def getEnemies(
        country: World.CountryId
      ): Iterable[World.CountryId] =
        getRelatedCountry(country, RelationStatus.WAR)

      private def getRelatedCountry(
        country: World.CountryId,
        status: RelationStatus
      ) =
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
