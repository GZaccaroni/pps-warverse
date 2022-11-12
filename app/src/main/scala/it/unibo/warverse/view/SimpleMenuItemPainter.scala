package it.unibo.warverse.view

import java.awt.Graphics2D
import java.awt.geom.RoundRectangle2D
import java.awt.Dimension
import java.awt.Color
import java.awt.FontMetrics

class SimpleMenuItemPainter extends MenuItemPainter:

  def getPreferredSize(g2d: Graphics2D, text: String): Dimension =
    return g2d
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
    var fm: FontMetrics = g2d.getFontMetrics()
    if isSelected then paintBackground(g2d, bounds, Color.BLUE, Color.WHITE)
    else if isFocused then paintBackground(g2d, bounds, Color.CYAN, Color.BLACK)
    else paintBackground(g2d, bounds, Color.DARK_GRAY, Color.WHITE)
    val x = bounds.getBounds.x + ((bounds.getBounds.width - fm
      .stringWidth(text)) / 2)
    val y =
      bounds.getBounds.y + ((bounds.getBounds.height - fm.getHeight) / 2) + fm.getAscent
    g2d.setColor(Color.WHITE)
    g2d.drawString(text, x, y)

  def paintBackground(
    g2d: Graphics2D,
    bounds: RoundRectangle2D,
    background: Color,
    foreground: Color
  ): Unit =
    g2d.setColor(background)
    g2d.fill(bounds)
    g2d.setColor(foreground)
    g2d.draw(bounds)
