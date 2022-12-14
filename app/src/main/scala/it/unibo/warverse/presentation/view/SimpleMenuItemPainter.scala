package it.unibo.warverse.presentation.view

import java.awt.geom.RoundRectangle2D
import java.awt.{Color, Dimension, Graphics2D}

class SimpleMenuItemPainter extends MenuItemPainter:

  override def preferredSize(g2d: Graphics2D, text: String): Dimension =
    g2d
      .getFontMetrics()
      .getStringBounds(text, g2d)
      .getBounds
      .getSize

  override def paint(
    g2d: Graphics2D,
    text: String,
    bounds: RoundRectangle2D,
    isSelected: Boolean,
    isFocused: Boolean
  ): Unit =
    val fm = g2d.getFontMetrics()
    if isSelected then paintBackground(g2d, bounds, Color.BLUE, Color.WHITE)
    else if isFocused then paintBackground(g2d, bounds, Color.CYAN, Color.BLACK)
    else paintBackground(g2d, bounds, Color.DARK_GRAY, Color.WHITE)
    val x = bounds.getBounds.x +
      ((bounds.getBounds.width - fm.stringWidth(text)) / 2)
    val y =
      bounds.getBounds.y + ((bounds.getBounds.height - fm.getHeight) / 2) + fm.getAscent
    g2d.setColor(Color.WHITE)
    g2d.drawString(text, x, y)

  private def paintBackground(
    g2d: Graphics2D,
    bounds: RoundRectangle2D,
    background: Color,
    foreground: Color
  ): Unit =
    g2d.setColor(background)
    g2d.fill(bounds)
    g2d.setColor(foreground)
    g2d.draw(bounds)
