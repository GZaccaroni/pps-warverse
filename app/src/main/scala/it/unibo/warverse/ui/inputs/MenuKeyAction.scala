package it.unibo.warverse.ui.inputs

import it.unibo.warverse.ui.view.{Menu, MenuActions}
import javax.swing.AbstractAction
import java.awt.event.ActionEvent
import javax.swing.JPanel

class MenuKeyAction(
  menuItems: Array[String],
  var selectMenuItem: String,
  panel: MenuActions,
  setMenuValue: String => Unit,
  moveValue: Integer
) extends AbstractAction:

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
    setMenuValue(selectMenuItem)

  override def actionPerformed(e: ActionEvent): Unit =
    selectMenuItem = panel.getMenuItems
    setIndex(moveValue)
    panel.repaint()
    panel.invalidate()