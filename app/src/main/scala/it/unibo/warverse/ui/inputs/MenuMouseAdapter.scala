package it.unibo.warverse.ui.inputs

import it.unibo.warverse.ui.view.{MainFrame, MenuHelp, MenuOptions, GameMap}
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.geom.RoundRectangle2D
import javax.swing.JPanel

class MenuMouseAdapter(
  menuItems: Array[String],
  panel: JPanel,
  var menuBounds: Map[String, RoundRectangle2D],
  setMenuValue: String => Unit,
  setFocusValue: String => Unit,
  setNewPanel: JPanel => Unit,
  mainFrame: MainFrame
) extends MouseAdapter:
  override def mouseClicked(e: MouseEvent): Unit =
    var newItem: String = ""
    menuItems.foreach(text =>
      if menuBounds.nonEmpty
      then
        val bounds: RoundRectangle2D = menuBounds(text)
        if bounds.contains(e.getPoint) then newItem = text
    )
    if newItem != null then
      setMenuValue(newItem)
      panel.repaint()
      if newItem != null then
        newItem match
          case "Start Game" => setNewPanel(new GameMap())
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
      if menuBounds.nonEmpty
      then
        val bounds: RoundRectangle2D = menuBounds(text)
        if bounds.contains(e.getPoint) then
          setFocusValue(text)
          this.panel.repaint()
    )
