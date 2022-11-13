package it.unibo.warverse.model.common

object Life:
  type Resources = Double
  trait LivingEntity:
    def alive: Boolean
