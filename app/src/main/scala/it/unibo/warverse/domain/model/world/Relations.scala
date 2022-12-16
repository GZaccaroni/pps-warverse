package it.unibo.warverse.domain.model.world

import scala.::

object Relations:
  /** The status of the relation between two countries
    */
  enum RelationStatus:
    case ALLIANCE, NEUTRAL, WAR

  type InterCountryRelation =
    ((World.CountryId, World.CountryId), RelationStatus)

  /** It keeps track of relations between countries
    */
  trait InterCountryRelations:
    /** Returns a set containing all the relations between countries
      * @return
      *   all the relations between countries
      */
    def relations: Set[InterCountryRelation]

    /** It returns a new [[InterCountryRelations]] object with `relation` added
      * to its relations
      * @param relation
      *   the relation to add to the object
      * @return
      *   the new object with the relation added
      */
    def withRelation(relation: InterCountryRelation): InterCountryRelations

    /** It returns a new [[InterCountryRelations]] object with `relation`
      * removed from its relations if it exists
      *
      * @param relation
      *   the relation to be removed from the object
      * @return
      *   the new object with the relation removed
      */
    def withoutRelation(relation: InterCountryRelation): InterCountryRelations

    /** It returns all the allies of a given country
      * @param country
      *   the country of which we want to find the allies
      * @return
      *   the allies of the country
      */
    def countryAllies(country: World.CountryId): Iterable[World.CountryId]

    /** It returns all the enemies of a given country
      *
      * @param country
      *   the country of which we want to find the enemies
      * @return
      *   the enemies of the country
      */
    def countryEnemies(country: World.CountryId): Iterable[World.CountryId]

    /** It returns the current relations between two country
      *
      * @param country1
      *   the first country of the pair
      * @param country2
      *   the second country of the pair
      * @return
      *   the relations between the countries
      */
    def relationStatus(
      country1: World.CountryId,
      country2: World.CountryId
    ): Set[RelationStatus]

  object InterCountryRelations:
    /** Factory that builds an instance of [[InterCountryRelations]] with the
      * given set of relations
      * @param relations
      *   the relations between countries
      * @return
      *   a new instance of [[InterCountryRelations]]
      */
    def apply(relations: Set[InterCountryRelation]): InterCountryRelations =
      InterCountryRelationsImpl(relations.map(_.normalized))

    private case class InterCountryRelationsImpl(
      override val relations: Set[InterCountryRelation]
    ) extends InterCountryRelations:

      checkIllegalRelation()

      override def withRelation(
        relation: InterCountryRelation
      ): InterCountryRelations =
        InterCountryRelationsImpl(relations + relation.normalized)

      override def withoutRelation(
        relation: InterCountryRelation
      ): InterCountryRelations =
        InterCountryRelationsImpl(
          relations - relation.normalized
        )

      override def countryAllies(
        country: World.CountryId
      ): Iterable[World.CountryId] =
        getRelatedCountry(country, RelationStatus.ALLIANCE)

      override def countryEnemies(
        country: World.CountryId
      ): Iterable[World.CountryId] =
        getRelatedCountry(country, RelationStatus.WAR)

      override def relationStatus(
        country1: World.CountryId,
        country2: World.CountryId
      ): Set[RelationStatus] =
        getRelatedStatus((country1, country2))

      private def getRelatedStatus(
        countries: (World.CountryId, World.CountryId)
      ) =
        val sortedCountries = countries.sorted
        relations.collect({ case (`sortedCountries`, status) =>
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

      private def checkIllegalRelation(): Unit =
        val illegalRelation =
          for
            (c, r) <- relations
            c1 = c.swap
            if relations.exists(x => (x._1 == c || x._1 == c1) && (x._2 != r))
          yield (c, r)
        if illegalRelation.nonEmpty then
          throw IllegalStateException("Invalid Relations")

    extension (field: InterCountryRelation)
      private def normalized: InterCountryRelation =
        (field._1.sorted, field._2)

    extension (field: (World.CountryId, World.CountryId))
      private def sorted: (World.CountryId, World.CountryId) =
        if field._2 < field._1 then field.swap
        else field
