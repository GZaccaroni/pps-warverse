package it.unibo.warverse.presentation.inputs

import it.unibo.warverse.presentation.view.MenuActions
import javax.swing.AbstractAction
import java.awt.event.ActionEvent

trait MenuKeyAction extends AbstractAction:
  def setIndex(delta: Int): Unit
  def moveValue: Int

object MenuKeyAction:
  def apply(
    panel: MenuActions,
    moveValue: Int
  ): MenuKeyAction =
    MenuKeyActionImpl(panel, moveValue)

  class MenuKeyActionImpl(
    private val panel: MenuActions,
    override val moveValue: Int
  ) extends MenuKeyAction:

    var index: Int = 0

    def setIndex(delta: Int): Unit =
      index = panel.menuItems.indexOf(panel.selectedItem) + delta
      if index < 0 then index = panel.menuItems.length - 1
      else if index >= panel.menuItems.length then index = 0
      panel.selectedItem = panel.menuItems(index)

    override def actionPerformed(e: ActionEvent): Unit =
      setIndex(moveValue)
      panel.repaint()
      panel.invalidate()
