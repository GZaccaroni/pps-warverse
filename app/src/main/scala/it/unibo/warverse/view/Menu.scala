package it.unibo.warverse

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.SwingUtilities
import javax.swing.WindowConstants.EXIT_ON_CLOSE
import javax.swing.JComponent
import java.util.HashMap
import javax.swing.JFrame
import java.awt.BorderLayout
import java.awt.geom.RoundRectangle2D
import java.awt.Toolkit
import java.awt.Image;
import scala.collection.mutable
import javax.swing.Action

class Menu(mainFrame: MainFrame) extends MenuActions:

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
  var enterAction: EnterAction = new EnterAction(this, setNewPanel(_))

  this.setBackground(Color.BLACK);

  this.setPreferredSize(getPreferredSize())

  var mouseAdapter: MenuMouseAdapter = new MenuMouseAdapter(
    menuItems,
    this,
    menuBounds,
    setMenuValue(_),
    setFocusValue(_),
    setNewPanel(_)
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

  override def getMenuItems(): String =
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
      .getImage("src/main/scala/it/unibo/warverse/assets/menuBackground.png")
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
