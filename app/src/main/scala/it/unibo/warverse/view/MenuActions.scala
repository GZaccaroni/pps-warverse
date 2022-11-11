package it.unibo.warverse

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

  var painter: SimpleMenuItemPainter = new SimpleMenuItemPainter();

  var menuBounds = mutable.HashMap[String, RoundRectangle2D]();

  val setMenuValue = (value: String) => selectMenuItem = value
  val setFocusValue = (value: String) => focusedItem = value
  val setNewPanel = (value: JPanel) => mainFrame.setPanel(value)

  var upAction: MenuKeyAction =
    new MenuKeyAction(menuItems, selectMenuItem, this, setMenuValue(_), -1);
  var downAction: MenuKeyAction =
    new MenuKeyAction(menuItems, selectMenuItem, this, setMenuValue(_), 1);
  var enterAction: EnterAction = new EnterAction(this, setNewPanel(_), mainFrame)

  this.setBackground(Color.BLACK);

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
  this.getActionMap().put("downAction", downAction)

  this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "upAction")
  this.getActionMap().put("upAction", upAction)

  this
    .getInputMap()
    .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enterAction")
  this.getActionMap().put("enterAction", enterAction)

  def getMenuItems(): String =
    return selectMenuItem

  override def getPreferredSize(): Dimension =
    return Toolkit.getDefaultToolkit().getScreenSize()

  override def invalidate(): Unit =
    menuBounds = null
    super.invalidate();

  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g);
    var img: Image = Toolkit
      .getDefaultToolkit()
      .getImage(path)
    var g2d: Graphics2D = g.create().asInstanceOf[Graphics2D]

    if menuBounds == null then
      menuBounds = mutable.HashMap[String, RoundRectangle2D]();
      var width = 0;
      var height = 0;
      menuItems.foreach(text =>
        val dim: Dimension = painter.getPreferredSize(g2d, text);
        width = Math.max(width, dim.width);
        height = Math.max(height, dim.height);
      )
      var x = (getWidth() - (width + 100)) / 2;
      var totalHeight = (height + 10) * menuItems.size;
      totalHeight += 5 * (menuItems.size - 1);

      var y = (getHeight() - totalHeight) / 2;
      menuItems.foreach(text =>
        menuBounds.put(
          text,
          new RoundRectangle2D.Double(x, y, width + 100, height + 10, 20, 20)
        );
        y += height + 35;
      )
      mouseAdapter.setBounds(menuBounds)
    g.drawImage(img, 0, 0, this.getSize().width, this.getSize().height, this)
    menuItems.foreach(text =>
      val bounds: RoundRectangle2D = menuBounds(text);
      val isSelected: Boolean = text.equals(selectMenuItem);
      val isFocused: Boolean = text.equals(focusedItem);
      painter.paint(g2d, text, bounds, isSelected, isFocused);
    )
    g2d.dispose();
