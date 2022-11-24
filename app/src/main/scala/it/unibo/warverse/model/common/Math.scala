package it.unibo.warverse.model.common

object Math:
  type Percentage = Double
  case class Precision(p: Double)
  implicit class DoubleWithAlmostEquals(val d: Double) extends AnyVal:
    def ~=(d2: Double)(using p: Precision): Boolean = (d - d2).abs < p.p
