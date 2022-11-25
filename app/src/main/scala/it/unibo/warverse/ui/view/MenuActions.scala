package it.unibo.warverse.ui.view

import it.unibo.warverse.ui.inputs.{
  EnterAction,
  MenuKeyAction,
  MenuMouseAdapter
}

import javax.swing.JPanel
import java.awt.geom.RoundRectangle2D
import scala.collection.mutable
import java.awt.Color
import javax.swing.SwingUtilities
import javax.swing.KeyStroke
import java.awt.event.KeyEvent
import java.awt.Dimension
import java.awt.Toolkit
import java.awt.Graphics
import java.awt.Image
import java.awt.Graphics2D
import java.net.URL
import it.unibo.warverse.ui.common.UIConstants
import javax.swing.JComponent

trait MenuActions extends JPanel:
  def mainFrame: MainFrame
  def setMenuValue(value: String): Unit
  def setFocusValue(value: String): Unit
  def setNewPanel(value: JPanel): Unit
  def upAction: MenuKeyAction
  def downAction: MenuKeyAction
  def enterAction: EnterAction
  def getMenuItems: String
  def updateMouseAdapter(): Unit

object MenuActions:
  def apply(mainFrame: MainFrame): MenuActions = MenuActionsImpl(mainFrame)

  class MenuActionsImpl(
    override val mainFrame: MainFrame
  ) extends MenuActions:

    val backgroundImageUrl: Option[URL] = Option(
      UIConstants.Resources.MainMenuBackground.url
    )

    val menuItems: Array[String] =
      Array("Start Game", "Options", "Help", "Exit")

    private var selectedMenuItem: String = menuItems(0)

    private var focusedItem: Option[String] = None

    private val painter: SimpleMenuItemPainter = SimpleMenuItemPainter()

    private var menuBounds: Option[Map[String, RoundRectangle2D]] = Some(
      Map.empty
    )

    override def setMenuValue(value: String): Unit =
      this.selectedMenuItem = value
    override def setFocusValue(value: String): Unit =
      this.focusedItem = Some(value)
    override def setNewPanel(value: JPanel): Unit =
      this.mainFrame.setPanel(value)

    override def getMenuItems: String = selectedMenuItem

    override def upAction: MenuKeyAction =
      MenuKeyAction(menuItems, selectedMenuItem, this, -1)
    override def downAction: MenuKeyAction =
      MenuKeyAction(menuItems, selectedMenuItem, this, 1)
    override def enterAction: EnterAction = EnterAction(this, mainFrame)

    override def updateMouseAdapter(): Unit =
      val mouseAdapter: MenuMouseAdapter = MenuMouseAdapter(
        menuItems,
        this,
        menuBounds.get,
        mainFrame
      )
      this.addMouseListener(mouseAdapter)
      this.addMouseMotionListener(mouseAdapter)

    this.updateMouseAdapter()
    this.setBackground(Color.BLACK)
    this.setPreferredSize(getPreferredSize())

    this
      .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
      .put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "downAction")
    this.getActionMap.put("downAction", downAction)

    this
      .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
      .put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "upAction")
    this.getActionMap.put("upAction", upAction)

    this
      .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
      .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enterAction")
    this.getActionMap.put("enterAction", enterAction)

    override def getPreferredSize: Dimension = UIConstants.borderMap
    override def invalidate(): Unit =
      menuBounds = None
      super.invalidate()

    override def paintComponent(g: Graphics): Unit =
      super.paintComponent(g)
      val backgroundImage =
        backgroundImageUrl.map(Toolkit.getDefaultToolkit.getImage(_))
      val g2d: Graphics2D = g.create().asInstanceOf[Graphics2D]
      var boxWidth: Int = 0
      var boxHeight: Int = 0
      if menuBounds.isEmpty then
        menuItems.foreach(text =>
          val dim: Dimension = painter.getPreferredSize(g2d, text);
          boxWidth = Math.max(boxWidth, dim.width);
          boxHeight = Math.max(boxHeight, dim.height);
        )
        val x = (getWidth - (boxWidth + 100)) / 2
        var totalHeight = (boxHeight + 10) * menuItems.length
        totalHeight += 5 * (menuItems.length - 1)

        val y = (getHeight - totalHeight) / 2
        menuBounds = Some(
          menuItems.zipWithIndex
            .map((text, index) =>
              (
                text,
                RoundRectangle2D.Double(
                  x,
                  y + (boxHeight + 35) * index,
                  boxWidth + 100,
                  boxHeight + 10,
                  20,
                  20
                )
              )
            )
            .toMap
        )
        this.updateMouseAdapter()
      backgroundImage.foreach(
        g.drawImage(_, 0, 0, this.getSize().width, this.getSize().height, this)
      )
      menuItems.foreach(text =>
        val bounds = menuBounds.get(text)
        val isSelected = text.equals(selectedMenuItem)
        val isFocused = focusedItem.contains(text)
        this.painter.paint(g2d, text, bounds, isSelected, isFocused)
      )
      g2d.dispose()
