package it.unibo.warverse.ui.inputs

import it.unibo.warverse.ui.view.MenuActions
import javax.swing.AbstractAction
import java.awt.event.ActionEvent
import javax.swing.JPanel

trait MenuKeyAction extends AbstractAction:
  def setIndex(delta: Integer): Unit
  def menuItems: Array[String]
  def selectMenuItem: String
  def panel: MenuActions
  def moveValue: Integer

object MenuKeyAction:
  def apply(
    menuItems: Array[String],
    selectMenuItem: String,
    panel: MenuActions,
    moveValue: Integer
  ): MenuKeyAction =
    MenuKeyActionImpl(menuItems, selectMenuItem, panel, moveValue)

  class MenuKeyActionImpl(
    override val menuItems: Array[String],
    var selectMenuItem: String,
    override val panel: MenuActions,
    override val moveValue: Integer
  ) extends MenuKeyAction:

    var index: Integer = 0

    def setIndex(delta: Integer): Unit =
      index = menuItems.indexOf(selectMenuItem) + delta
      if index < 0 then
        index = menuItems.length - 1
        selectMenuItem = menuItems(menuItems.length - 1)
      else if index >= menuItems.length then
        selectMenuItem = menuItems(0)
        index = 0
      else selectMenuItem = menuItems(index)
      panel.setMenuValue(selectMenuItem)

    override def actionPerformed(e: ActionEvent): Unit =
      selectMenuItem = panel.getMenuItems
      setIndex(moveValue)
      panel.repaint()
      panel.invalidate()
