package it.unibo.warverse.ui.inputs

import it.unibo.warverse.ui.view.{
  MainFrame,
  MenuHelp,
  MenuOptions,
  GameMap,
  GamePanel
}
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.geom.RoundRectangle2D
import javax.swing.JPanel
import it.unibo.warverse.controller.GameStateController

trait MenuMouseAdapter extends MouseAdapter:
  def menuItems: Array[String]
  def panel: JPanel
  def menuBounds: Map[String, RoundRectangle2D]
  def setMenuValue: String => Unit
  def setFocusValue: String => Unit
  def setNewPanel: JPanel => Unit
  def mainFrame: MainFrame

object MenuMouseAdapter:
  def apply(
    menuItems: Array[String],
    panel: JPanel,
    menuBounds: Map[String, RoundRectangle2D],
    setMenuValue: String => Unit,
    setFocusValue: String => Unit,
    setNewPanel: JPanel => Unit,
    mainFrame: MainFrame
  ): MenuMouseAdapter =
    MenuMouseAdapterImpl(
      menuItems,
      panel,
      menuBounds,
      setMenuValue,
      setFocusValue,
      setNewPanel,
      mainFrame
    )

  private class MenuMouseAdapterImpl(
    override val menuItems: Array[String],
    override val panel: JPanel,
    override val menuBounds: Map[String, RoundRectangle2D],
    override val setMenuValue: String => Unit,
    override val setFocusValue: String => Unit,
    override val setNewPanel: JPanel => Unit,
    override val mainFrame: MainFrame
  ) extends MenuMouseAdapter:

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
        this.panel.repaint()
        if newItem != null then
          newItem match
            case "Start Game" => GameStateController().setMain(mainFrame)
            case "Options"    => mainFrame.setPanel(MenuOptions())
            case "Help"       => mainFrame.setPanel(MenuHelp(mainFrame))
            case "Exit"       => System.exit(0)
            case _            =>

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
