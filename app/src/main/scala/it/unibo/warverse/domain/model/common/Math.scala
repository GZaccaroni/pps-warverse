package it.unibo.warverse.domain.model.common

import scala.annotation.targetName

object Math:
  type Percentage = Double

  /** Double comparison precision
    * @param p
    *   actual precision value
    */
  case class Precision(p: Double)
  implicit class DoubleWithAlmostEquals(val d: Double) extends AnyVal:
    /** Compares two value given a precision
      * @param d2
      *   the second value to compare
      * @param p
      *   the precision to use
      * @return
      *   if the two value are the same taking precision into account
      */
    @targetName("almostEquals")
    def ~=(d2: Double)(using p: Precision): Boolean = (d - d2).abs < p.p
