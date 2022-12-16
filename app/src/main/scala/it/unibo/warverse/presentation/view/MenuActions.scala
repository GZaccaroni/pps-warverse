package it.unibo.warverse.presentation.view

import it.unibo.warverse.presentation.inputs.{
  EnterAction,
  MenuKeyAction,
  MenuMouseAdapter
}
import it.unibo.warverse.presentation.common.UIConstants
import it.unibo.warverse.presentation.model.MenuItems.*
import java.awt.geom.RoundRectangle2D
import java.awt.event.KeyEvent
import java.awt.{Graphics2D, Graphics, Toolkit, Dimension, Color}
import java.net.URL
import javax.swing.{JComponent, JPanel, KeyStroke}

trait MenuActions extends JPanel:
  def mainFrame: MainFrame
  def menuItems: Seq[MenuItem]
  var selectedItem: MenuItem
  var focusedItem: Option[MenuItem]
  def setNewPanel(value: JPanel): Unit
  def upAction: MenuKeyAction
  def downAction: MenuKeyAction
  def enterAction: EnterAction
  def updateMouseAdapter(): Unit

object MenuActions:
  def apply(mainFrame: MainFrame): MenuActions = MenuActionsImpl(mainFrame)

  class MenuActionsImpl(
    override val mainFrame: MainFrame
  ) extends MenuActions:

    val backgroundImageUrl: Option[URL] = Option(
      UIConstants.Resources.MainMenuBackground.url
    )

    val menuItems = MainMenuItem.values.toSeq

    var selectedItem: MenuItem = MainMenuItem.StartGame

    var focusedItem: Option[MenuItem] = None

    private val painter: SimpleMenuItemPainter = SimpleMenuItemPainter()

    private var menuBounds: Option[Map[MenuItem, RoundRectangle2D]] = Some(
      Map.empty
    )
    override def setNewPanel(value: JPanel): Unit =
      this.mainFrame.setPanel(value)

    override def upAction: MenuKeyAction =
      MenuKeyAction(this, -1)
    override def downAction: MenuKeyAction =
      MenuKeyAction(this, 1)
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
      if menuBounds.isEmpty then

        val boxDimension = menuItems.foldLeft(Dimension()) {
          (boxDim, menuItem) =>
            val dim: Dimension = painter.preferredSize(g2d, menuItem.label)
            Dimension(
              Math.max(boxDim.width, dim.width),
              Math.max(boxDim.height, dim.height)
            )
        }
        val x = (getWidth - (boxDimension.width + 100)) / 2
        val totalHeight = (boxDimension.height + 15) * menuItems.length - 5

        val y = (getHeight - totalHeight) / 2
        menuBounds = Some(
          menuItems.zipWithIndex
            .map((text, index) =>
              (
                text,
                RoundRectangle2D.Double(
                  x,
                  y + (boxDimension.height + 35) * index,
                  boxDimension.width + 100,
                  boxDimension.height + 10,
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
      menuItems.foreach(menuItem =>
        val bounds = menuBounds.get(menuItem)
        val isSelected = menuItem.equals(selectedItem)
        val isFocused = focusedItem.contains(menuItem)
        this.painter.paint(g2d, menuItem.label, bounds, isSelected, isFocused)
      )
      g2d.dispose()
