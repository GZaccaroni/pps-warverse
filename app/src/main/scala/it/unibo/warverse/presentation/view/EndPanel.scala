package it.unibo.warverse.presentation.view

import javax.swing.{JPanel, JTextPane, BoxLayout}
import java.awt.{Font, Color}
import javax.swing.text.{StyledDocument, SimpleAttributeSet, StyleConstants}
import it.unibo.warverse.domain.model.Environment
import javax.swing.JScrollPane

trait EndPanel extends JPanel:
  def environment: Environment
  def setPaneAttributes(textPane: JTextPane): Unit

object EndPanel:
  def apply(environment: Environment): EndPanel = EndPanelImpl(environment)

  private class EndPanelImpl(override val environment: Environment)
      extends EndPanel:
    private val title: JTextPane = JTextPane()
    private val stats: JTextPane = JTextPane()
    private val countries = environment.countries
    private val gameStats: JScrollPane = JScrollPane(stats)
    private val warsExists: Boolean =
      environment.interCountryRelations.hasOngoingWars
    this.setLayout(BoxLayout(this, BoxLayout.Y_AXIS))

    if countries.nonEmpty && !warsExists then
      title.setText(s"Winners: ${countries.map(_.id).mkString(", ")}")
      stats.setText(
        countries.foldLeft("") { (text, c) =>
          s"${text}Country: ${c.id}\nRemaining Army Units: ${c.armyUnits.size}\nRemaining Citizens: ${c.citizens}\nRemaining Resources: ${String
              .format("%.02f", c.resources)}\n\n"
        }
      )
    else title.setText("Nobody Win War")
    if warsExists then
      title.setText(
        "Simulation interrupted while wars were active, there are no winners"
      )

    title.setText(s"${title.getText()}\nDay passed: ${environment.day}")

    title.setFont(Font("Serif", Font.PLAIN, 32))
    setPaneAttributes(title)
    this.add(title)
    stats.setFont(Font("Serif", Font.PLAIN, 20))
    setPaneAttributes(stats)
    gameStats.setBorder(null)
    gameStats.setAlignmentY(StyleConstants.ALIGN_CENTER)
    this.add(gameStats)

    override def setPaneAttributes(textPane: JTextPane): Unit =
      textPane.setEditable(false)
      textPane.setBackground(Color.BLACK)
      textPane.setForeground(Color.WHITE)
      val documentStyle: StyledDocument = textPane.getStyledDocument
      val centerAttribute: SimpleAttributeSet = SimpleAttributeSet()
      StyleConstants.setAlignment(centerAttribute, StyleConstants.ALIGN_CENTER)
      documentStyle.setParagraphAttributes(
        0,
        documentStyle.getLength,
        centerAttribute,
        false
      )
