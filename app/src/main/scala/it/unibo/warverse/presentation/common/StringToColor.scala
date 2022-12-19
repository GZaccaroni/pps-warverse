package it.unibo.warverse.presentation.common

import java.awt.Color

extension (field: String)
  /** Generates a [[Color]]color from a string
    * @return
    *   color
    */
  private[presentation] def toColor: Color =
    val hash: Int = field.hashCode

    val r: Int = (hash & 0xff0000) >> 16
    val g: Int = (hash & 0x00ff00) >> 8
    val b: Int = hash & 0x0000ff

    Color(r, g, b)
