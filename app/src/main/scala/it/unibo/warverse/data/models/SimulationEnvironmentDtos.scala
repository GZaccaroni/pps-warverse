package it.unibo.warverse.data.models

import it.unibo.warverse.data.models.WorldDtos
import it.unibo.warverse.domain.model.common.validation.Validation.*
import it.unibo.warverse.domain.model.common.validation.CommonValidators.*

private[data] object SimulationEnvironmentDtos:
  case class SimulationEnvironmentDto(
    countries: Seq[WorldDtos.CountryDto]
  ) extends Validatable:
    override def validationErrors: List[ValidationError] =
      val countryIdentifiers = countries.map(_.id)
      countries.validationErrors :::
        (countryIdentifiers must ContainNoDuplicates()) :::
        countries
          .flatMap(country =>
            (
              country.relations.enemies ++ country.relations.allies
            ).forall(
              countryIdentifiers.contains(_)
            ) orElse "Not all relationships have countries defined"
          )
          .toList
