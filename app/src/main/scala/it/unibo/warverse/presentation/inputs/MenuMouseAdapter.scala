package it.unibo.warverse.presentation.inputs

import it.unibo.warverse.presentation.view.{
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
import it.unibo.warverse.presentation.controllers.GameStateController
import it.unibo.warverse.presentation.view.MenuActions

trait MenuMouseAdapter extends MouseAdapter:
  def menuItems: Seq[String]
  def panel: MenuActions
  def menuBounds: Map[String, RoundRectangle2D]
  def mainFrame: MainFrame

object MenuMouseAdapter:
  def apply(
    menuItems: Seq[String],
    panel: MenuActions,
    menuBounds: Map[String, RoundRectangle2D],
    mainFrame: MainFrame
  ): MenuMouseAdapter =
    MenuMouseAdapterImpl(
      menuItems,
      panel,
      menuBounds,
      mainFrame
    )

  private class MenuMouseAdapterImpl(
    override val menuItems: Seq[String],
    override val panel: MenuActions,
    override val menuBounds: Map[String, RoundRectangle2D],
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
        panel.setMenuValue(newItem)
        if newItem != null then
          newItem match
            case "Start Game" => GameStateController(mainFrame).setPanel()
            case "Options"    => mainFrame.setPanel(MenuOptions())
            case "Help"       => mainFrame.setPanel(MenuHelp(mainFrame))
            case "Exit"       => System.exit(0)
            case _            =>

    override def mouseMoved(e: MouseEvent): Unit =
      mouseTrigger(e)
    override def mouseEntered(e: MouseEvent): Unit =
      mouseTrigger(e)

    def mouseTrigger(e: MouseEvent): Unit =
      panel.setFocusValue("")
      menuItems.foreach(text =>
        if menuBounds.nonEmpty
        then
          val bounds: RoundRectangle2D = menuBounds(text)
          if bounds.contains(e.getPoint) then
            panel.setFocusValue(text)
            this.panel.repaint()
      )
