package it.unibo.warverse.data.models

import it.unibo.warverse.data.models.WorldDtos
import it.unibo.warverse.domain.model.common.Validation.*
import it.unibo.warverse.domain.model.world.World

object SimulationConfigDtos:
  case class SimulationConfigDto(
    countries: List[WorldDtos.CountryDto]
  ) extends Validatable:
    override def validate(): Unit =
      given ValidatedEntity = ValidatedEntity(this.getClass.getTypeName)
      countries.foreach(country =>
        country.validate()
        val relationships =
          country.relations.enemies ++ country.relations.allies
        if relationships.contains(country.id) then
          throw ValidationException(
            s"Country ${country.id} can't be allied or in war with himself"
          )
        if relationships.exists(relId => !countries.exists(relId == _.id)) then
          throw ValidationException(
            s"Not all relationships have countries defined"
          )
      )
      if countries.map(_.id).toSet.size != countries.size then
        throw ValidationException(
          s"Some countries have the same id"
        )
