package it.unibo.warverse.domain.model.common

object Resources:
  /** The resources of a country
    */
  type Resources = Double

  /** Represent an entity with periodic consume of resources. */
  trait ResourcesConsumer:
    /** The quantity of resources that consume every day
      * @return
      *   a representation of the daily resource consume
      */
    def dailyConsume: Resources

  /** Represent an entity with periodic production of resources. */
  trait ResourcesProducer:
    /** The quantity of resources that produce every day
      * @return
      *   a representation of the daily resource production
      */
    def dailyProduction: Resources
