package it.unibo.warverse.domain.model.common

import it.unibo.warverse.domain.model.common.Geometry.Polygon2D
import it.unibo.warverse.domain.model.world.World.Country

object DomainExample:
  val countryA: Country =
    Country("ID_1", "A", 40, List.empty, 0.0, Polygon2D(List.empty))
  val countryB: Country =
    Country("ID_2", "B", 10, List.empty, 0.0, Polygon2D(List.empty))
  val countryC: Country =
    Country("ID_3", "C", 20, List.empty, 0.0, Polygon2D(List.empty))
