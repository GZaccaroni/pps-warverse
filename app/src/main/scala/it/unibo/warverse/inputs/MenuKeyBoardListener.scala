package it.unibo.warverse

import java.awt.event.KeyListener
import java.awt.event.KeyEvent
import javax.swing.JPanel

class MenuKeyBoardListener(
  menuItems: Array[String],
  selectMenuItemProp: String,
  panel: JPanel,
  setMenuValue: String => Unit
) extends KeyListener:

  var index = 0
  var selectMenuItem = selectMenuItemProp

  def setIndex(delta: Integer): Unit =
    index = menuItems.indexOf(selectMenuItem) + delta
    if index < 0 then
      index = menuItems.size - 1
      selectMenuItem = menuItems(menuItems.size - 1)
    else if index >= menuItems.size then
      selectMenuItem = menuItems(0)
      index = 0
    else selectMenuItem = menuItems(index)

    setMenuValue(selectMenuItem)
  override def keyTyped(e: KeyEvent): Unit = ()

  override def keyPressed(e: KeyEvent): Unit =
    e.getKeyCode() match
      case KeyEvent.VK_ENTER =>
        println(index)
        index match
          case 0 => println("START")
          case 1 => println("OPTION")
          case 2 => println("HELP")
          case 3 => System.exit(0)
          case _ =>
      case KeyEvent.VK_W | KeyEvent.VK_UP   => setIndex(-1)
      case KeyEvent.VK_S | KeyEvent.VK_DOWN => setIndex(1)
      case _                                => ()
    panel.repaint()
    panel.invalidate()

  override def keyReleased(e: KeyEvent): Unit = ()
