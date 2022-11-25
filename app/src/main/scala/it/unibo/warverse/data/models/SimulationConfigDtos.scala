package it.unibo.warverse.data.models

import it.unibo.warverse.data.models.WorldDtos
import it.unibo.warverse.domain.model.world.World

object SimulationConfigDtos:
  case class SimulationConfigDto(
    countries: List[WorldDtos.CountryDto]
  )
