package it.unibo.warverse.view

import it.unibo.warverse.inputs.MenuKeyAction
import it.unibo.warverse.inputs.EnterAction
import it.unibo.warverse.inputs.MenuMouseAdapter
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

class MenuActions(mainFrame: MainFrame) extends JPanel:

  var path = ""
  def setBackGroundImage(path: String): Unit =
    this.path = path

  var menuItems: Array[String] =
    Array("Start Game", "Options", "Help", "Exit")

  var selectMenuItem: String = menuItems(0)

  var focusedItem: String = ""

  var painter: SimpleMenuItemPainter = new SimpleMenuItemPainter()

  private var boxWidth: Int = 0
  private var boxHeight: Int = 0
  private var menuBounds = mutable.HashMap[String, RoundRectangle2D]()

  private val setMenuValue = (value: String) => selectMenuItem = value
  private val setFocusValue = (value: String) => focusedItem = value
  private val setNewPanel = (value: JPanel) => mainFrame.setPanel(value)

  var upAction: MenuKeyAction =
    new MenuKeyAction(menuItems, selectMenuItem, this, setMenuValue(_), -1)
  var downAction: MenuKeyAction =
    new MenuKeyAction(menuItems, selectMenuItem, this, setMenuValue(_), 1)
  var enterAction: EnterAction =
    new EnterAction(this, setNewPanel(_), mainFrame)

  this.setBackground(Color.BLACK)

  this.setPreferredSize(getPreferredSize())

  var mouseAdapter: MenuMouseAdapter = new MenuMouseAdapter(
    menuItems,
    this,
    menuBounds,
    setMenuValue(_),
    setFocusValue(_),
    setNewPanel(_),
    mainFrame
  )

  this.addMouseListener(mouseAdapter)
  this.addMouseMotionListener(mouseAdapter)

  this
    .getInputMap()
    .put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "downAction")
  this.getActionMap.put("downAction", downAction)

  this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "upAction")
  this.getActionMap.put("upAction", upAction)

  this
    .getInputMap()
    .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enterAction")
  this.getActionMap.put("enterAction", enterAction)

  def getMenuItems: String = selectMenuItem

  override def getPreferredSize: Dimension =
    Toolkit.getDefaultToolkit.getScreenSize

  override def invalidate(): Unit =
    menuBounds = null
    super.invalidate()

  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)
    val img: Image = Toolkit.getDefaultToolkit
      .getImage(path)
    val g2d: Graphics2D = g.create().asInstanceOf[Graphics2D]

    if menuBounds == null then
      menuBounds = mutable.HashMap[String, RoundRectangle2D]()
      menuItems.foreach(text =>
        val dim: Dimension = painter.getPreferredSize(g2d, text)
        this.boxWidth = Math.max(boxWidth, dim.width)
        this.boxHeight = Math.max(boxHeight, dim.height)
      )
      val x = (getWidth - (this.boxWidth + 100)) / 2
      var totalHeight = (boxHeight + 10) * menuItems.length
      totalHeight += 5 * (menuItems.length - 1)

      var y = (getHeight - totalHeight) / 2
      menuItems.foreach(text =>
        menuBounds.put(
          text,
          new RoundRectangle2D.Double(
            x,
            y,
            boxWidth + 100,
            boxHeight + 10,
            20,
            20
          )
        )
        y += this.boxHeight + 35
      )
      mouseAdapter.setBounds(menuBounds)
    g.drawImage(img, 0, 0, this.getSize().width, this.getSize().height, this)
    menuItems.foreach(text =>
      val bounds: RoundRectangle2D = menuBounds(text)
      val isSelected: Boolean = text.equals(selectMenuItem)
      val isFocused: Boolean = text.equals(focusedItem)
      this.painter.paint(g2d, text, bounds, isSelected, isFocused)
    )
    g2d.dispose()
