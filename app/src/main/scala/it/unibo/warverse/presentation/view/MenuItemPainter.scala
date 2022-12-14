package it.unibo.warverse.presentation.view

import java.awt.Graphics2D
import java.awt.geom.RoundRectangle2D
import java.awt.Dimension

trait MenuItemPainter:
  def paint(
    g2d: Graphics2D,
    text: String,
    bounds: RoundRectangle2D,
    isSelected: Boolean,
    isFocused: Boolean
  ): Unit

  def preferredSize(g2d: Graphics2D, text: String): Dimension
