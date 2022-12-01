package it.unibo.warverse.domain.model.world

import it.unibo.warverse.domain.model.world.World.Country

import scala.::
import javax.swing.JOptionPane
import it.unibo.warverse.domain.model.world.World.CountryId

object Relations:

  enum RelationStatus:
    case ALLIANCE, NEUTRAL, WAR

  type InterstateRelation = ((World.CountryId, World.CountryId), RelationStatus)

  trait InterstateRelations:

    def relations: List[InterstateRelation]

    def withRelation(relation: InterstateRelation): InterstateRelations

    def withoutRelation(relation: InterstateRelation): InterstateRelations

    def getAllies(country: World.CountryId): Iterable[World.CountryId]

    def getEnemies(country: World.CountryId): Iterable[World.CountryId]

    def getStatus(
      country1: World.CountryId,
      country2: World.CountryId
    ): List[RelationStatus]

  object InterstateRelations:
    def apply(relations: List[InterstateRelation]): InterstateRelations =
      InterstateRelationsImpl(relations).dropDuplicates

    private case class InterstateRelationsImpl(
      override val relations: List[InterstateRelation]
    ) extends InterstateRelations:

      checkIllegalRelation()

      override def withRelation(
        relation: InterstateRelation
      ): InterstateRelations =
        InterstateRelationsImpl(relations :+ relation)

      override def withoutRelation(
        relation: InterstateRelation
      ): InterstateRelations =
        InterstateRelationsImpl(
          relations.filterNot(r =>
            r == relation ||
              (r._1.swap, r._2) == relation
          )
        )

      override def getAllies(
        country: World.CountryId
      ): Iterable[World.CountryId] =
        getRelatedCountry(country, RelationStatus.ALLIANCE)

      override def getEnemies(
        country: World.CountryId
      ): Iterable[World.CountryId] =
        getRelatedCountry(country, RelationStatus.WAR)

      override def getStatus(
        country1: World.CountryId,
        country2: World.CountryId
      ): List[RelationStatus] =
        getRelatedStatus(country1, country2)

      private def getRelatedStatus(
        country1: World.CountryId,
        country2: World.CountryId
      ) =
        relations.collect({
          case ((`country1`, `country2`), status) =>
            status
          case ((`country2`, `country1`), status) =>
            status
        })

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
