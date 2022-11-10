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

class Menu(mainFrame : JFrame) extends JPanel:

  var menuItems: Array[String] =
    Array("Start Game", "Options", "Help", "Exit")
  var selectMenuItem: String = menuItems(0)

  var focusedItem: String = ""

  var painter: SimpleMenuItemPainter = new SimpleMenuItemPainter();

  var menuBounds = scala.collection.mutable.Map[String, RoundRectangle2D]()

  this.setBackground(Color.BLACK);

  this.setPreferredSize(getPreferredSize())

  var mouseAdapter: MouseAdapter = new MouseAdapter():
    override def mouseClicked(e: MouseEvent): Unit =
      var newItem: String = null;
      menuItems.foreach(text =>
        val bounds: RoundRectangle2D = menuBounds(text);
        if bounds.contains(e.getPoint()) then newItem = text;
      )
      if newItem != null && !newItem.equals(selectMenuItem) then
        selectMenuItem = newItem;
        repaint();
      if newItem != null && newItem.equals("Exit") then System.exit(0)

    override def mouseMoved(e: MouseEvent): Unit =
      focusedItem = null;
      menuItems.foreach(text =>
        val bounds: RoundRectangle2D = menuBounds(text);
        if bounds.contains(e.getPoint()) then
          focusedItem = text;
          repaint();
      )
    override def mouseEntered(e: MouseEvent): Unit =
      focusedItem = null;
      menuItems.foreach(text =>
        val bounds: RoundRectangle2D = menuBounds(text);
        if bounds.contains(e.getPoint()) then
          focusedItem = text;
          repaint();
      )

    val im: InputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    val am: ActionMap = getActionMap();

    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "arrowDown");
    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "arrowUp");
    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");

    am.put("arrowDown", new MenuAction(1));
    am.put("arrowUp", new MenuAction(-1));
    am.put("enter", new MenuAction(0));

  this.addMouseListener(mouseAdapter)
  this.addMouseMotionListener(mouseAdapter);

  override def invalidate(): Unit =
    menuBounds = null;
    super.invalidate();

  override def getPreferredSize(): Dimension =
    return Toolkit.getDefaultToolkit().getScreenSize()

  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g);
    var img: Image = Toolkit.getDefaultToolkit().getImage("src/main/scala/it/unibo/warverse/assets/menuBackground.png")
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
    g.drawImage(img, 0, 0, this.getSize().width, this.getSize().height, this)
    menuItems.foreach(text =>
      val bounds: RoundRectangle2D = menuBounds(text);
      val isSelected: Boolean = text.equals(selectMenuItem);
      val isFocused: Boolean = text.equals(focusedItem);
      painter.paint(g2d, text, bounds, isSelected, isFocused);
    )
    g2d.dispose();

  class MenuAction(delta: Integer) extends AbstractAction:

    override def actionPerformed(e: ActionEvent): Unit =
      var index = menuItems.indexOf(selectMenuItem);
      if delta == 0 then
        index match
          case 0 => println("START")
          case 1 => println("OPTION")
          case 2 => mainFrame.setContentPane(new Menu(mainFrame))
                    mainFrame.validate()
          case 3 =>
            System.exit(0)
      if index < 0 then selectMenuItem = menuItems(0);
      index += delta;
      if index < 0 then selectMenuItem = menuItems(menuItems.size - 1);
      else if index >= menuItems.size then selectMenuItem = menuItems(0);
      else selectMenuItem = menuItems(index);
      repaint();
