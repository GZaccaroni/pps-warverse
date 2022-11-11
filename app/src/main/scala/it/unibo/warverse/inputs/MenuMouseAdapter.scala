package it.unibo.warverse

import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.geom.RoundRectangle2D
import javax.swing.JPanel

class MenuMouseAdapter(
  menuItems: Array[String],
  panel: JPanel,
  var menuBounds: scala.collection.mutable.Map[String, RoundRectangle2D],
  setMenuValue: String => Unit,
  setFocusValue: String => Unit,
  setNewPanel: JPanel => Unit,
  mainFrame: MainFrame
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
    if newItem != null then
      setMenuValue(newItem)
      panel.repaint()
      if newItem != null then
        newItem match
          case "Options" => setNewPanel(new MenuOptions())
          case "Help"    => setNewPanel(new MenuHelp(mainFrame))
          case "Exit"    => System.exit(0)
          case _         =>

  override def mouseMoved(e: MouseEvent): Unit =
    mouseTrigger(e)
  override def mouseEntered(e: MouseEvent): Unit =
    mouseTrigger(e)

  def mouseTrigger(e: MouseEvent): Unit =
    setFocusValue("")
    menuItems.foreach(text =>
      if !menuBounds.isEmpty
      then
        val bounds: RoundRectangle2D = menuBounds(text)
        if bounds.contains(e.getPoint()) then
          setFocusValue(text)
          panel.repaint()
    )
