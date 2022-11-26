package it.unibo.warverse.domain.model.common

object Life:
  type Resources = Double
  trait LivingEntity:
    def alive: Boolean
