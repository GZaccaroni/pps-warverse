package it.unibo.warverse.presentation.inputs

import it.unibo.warverse.presentation.view.{
  GameMap,
  GamePanel,
  MainFrame,
  MenuHelp,
  MenuOptions
}

import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.geom.RoundRectangle2D
import javax.swing.JPanel
import it.unibo.warverse.presentation.controllers.GameStateController
import it.unibo.warverse.presentation.model.MenuItems.*
import it.unibo.warverse.presentation.view.MenuActions

trait MenuMouseAdapter extends MouseAdapter:
  def menuItems: Seq[MenuItem]
  def panel: MenuActions
  def menuBounds: Map[MenuItem, RoundRectangle2D]
  def mainFrame: MainFrame

object MenuMouseAdapter:
  def apply(
    menuItems: Seq[MenuItem],
    panel: MenuActions,
    menuBounds: Map[MenuItem, RoundRectangle2D],
    mainFrame: MainFrame
  ): MenuMouseAdapter =
    MenuMouseAdapterImpl(
      menuItems,
      panel,
      menuBounds,
      mainFrame
    )

  private class MenuMouseAdapterImpl(
    override val menuItems: Seq[MenuItem],
    override val panel: MenuActions,
    override val menuBounds: Map[MenuItem, RoundRectangle2D],
    override val mainFrame: MainFrame
  ) extends MenuMouseAdapter:

    override def mouseClicked(e: MouseEvent): Unit =
      val itemClicked = itemAtPoint(e.getPoint)
      itemClicked match
        case Some(itemName) =>
          panel.selectedItem = itemName
          itemName match
            case MainMenuItem.StartGame =>
              GameStateController(mainFrame).setPanel()
            case MainMenuItem.Options => mainFrame.setPanel(MenuOptions())
            case MainMenuItem.Help    => mainFrame.setPanel(MenuHelp(mainFrame))
            case MainMenuItem.Exit    => System.exit(0)
            case _                    =>
        case _ =>

    override def mouseMoved(e: MouseEvent): Unit =
      mouseTrigger(e)

    override def mouseEntered(e: MouseEvent): Unit =
      mouseTrigger(e)

    private def mouseTrigger(e: MouseEvent): Unit =
      val itemHovered = itemAtPoint(e.getPoint)
      panel.focusedItem = itemHovered
      this.panel.repaint()

    private def itemAtPoint(point: Point): Option[MenuItem] =
      menuItems.find(text => menuBounds.get(text).exists(_.contains(point)))
