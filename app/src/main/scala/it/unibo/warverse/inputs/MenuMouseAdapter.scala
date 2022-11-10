package it.unibo.warverse

import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.geom.RoundRectangle2D
import javax.swing.JPanel

class MenuMouseAdapter(
  menuItems: Array[String],
  selectMenuItem: String,
  panel: JPanel,
  var menuBounds: scala.collection.mutable.Map[String, RoundRectangle2D],
  setMenuValue: String => Unit,
  setFocusValue: String => Unit
) extends MouseAdapter:

  def setBounds(
    value: scala.collection.mutable.Map[String, RoundRectangle2D]
  ): Unit =
    menuBounds = value
  override def mouseClicked(e: MouseEvent): Unit =
    var newItem: String = ""
    menuItems.foreach(text =>
      if !menuBounds.isEmpty
      then
        val bounds: RoundRectangle2D = menuBounds(text)
        if bounds.contains(e.getPoint()) then newItem = text
    )
    if newItem != null && !newItem.equals(selectMenuItem) then
      setMenuValue(newItem)
      panel.repaint()
      if newItem != null && newItem.equals("Exit") then System.exit(0)

  override def mouseMoved(e: MouseEvent): Unit =
    setFocusValue("")
    menuItems.foreach(text =>
      println(menuBounds)
      if !menuBounds.isEmpty
      then
        val bounds: RoundRectangle2D = menuBounds(text)
        if bounds.contains(e.getPoint()) then
          setFocusValue(text)
          panel.repaint()
    )
  override def mouseEntered(e: MouseEvent): Unit =
    setFocusValue("")
    menuItems.foreach(text =>
      if !menuBounds.isEmpty
      then
        val bounds: RoundRectangle2D = menuBounds(text)
        if bounds.contains(e.getPoint()) then
          setFocusValue(text)
          panel.repaint()
    )
