package it.unibo.warverse

import it.unibo.warverse.view.MainFrame
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers
import javax.swing.JPanel
import javax.swing.JFrame

class LauncherTest extends AnyFunSuite with Matchers:
  test("A MainFrame is an instance of JFrame and is not null") {
    val mainFrame = new MainFrame()
    assert(mainFrame.isInstanceOf[JFrame])
    assert(mainFrame != null)
  }
