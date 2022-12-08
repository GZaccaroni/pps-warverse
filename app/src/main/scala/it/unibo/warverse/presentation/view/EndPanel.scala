package it.unibo.warverse.presentation.view

import javax.swing.{JPanel, JTextPane, BoxLayout}
import java.awt.{Font, Color}
import javax.swing.text.{StyledDocument, SimpleAttributeSet, StyleConstants}
import it.unibo.warverse.domain.model.Environment
import javax.swing.JScrollPane

trait EndPanel extends JPanel:
  def environment: Environment

object EndPanel:
  def apply(environment: Environment): EndPanel = EndPanelImpl(environment)

  private class EndPanelImpl(override val environment: Environment)
      extends EndPanel:
    private val title: JTextPane = JTextPane()
    private val stats: JTextPane = JTextPane()
    private val countries = environment.countries
    private val gameStats: JScrollPane = JScrollPane(stats)
    this.setLayout(BoxLayout(this, BoxLayout.Y_AXIS))

    if warsExists(environment) then
      title.setText(
        "Simulation interrupted while wars were active, there are no winners"
      )
    else if countries.size > 0 then
      title.setText("Winners are: ")
      countries.foreach(c =>
        title.setText(title.getText + c.id)
        if countries.size - 1 != countries.indexOf(c) then
          title.setText(title.getText() + ", ")
      )
    else title.setText("Nobody Win in War")

    title.setFont(Font("TimesRoman", Font.PLAIN, 32))
    setPaneAttributes(title)
    this.add(title)

    countries.foreach(c =>
      stats.setText(
        stats
          .getText() + "Country: " + c.id + "\nRemaining Army Units: " + c.armyUnits.size + "\nCitizen Remaining: " + c.citizens + "\nResources Remaining: " + c.resources + "\n\n"
      )
    )

    stats.setFont(Font("TimesRoman", Font.PLAIN, 20))
    setPaneAttributes(stats)
    this.add(gameStats)

    private def setPaneAttributes(textPane: JTextPane): Unit =
      textPane.setBackground(Color.BLACK)
      textPane.setForeground(Color.WHITE)
      val documentStyle: StyledDocument = textPane.getStyledDocument()
      val centerAttribute: SimpleAttributeSet = SimpleAttributeSet()
      StyleConstants.setAlignment(centerAttribute, StyleConstants.ALIGN_CENTER)
      documentStyle.setParagraphAttributes(
        0,
        documentStyle.getLength(),
        centerAttribute,
        false
      )

    private def warsExists(
      environment: Environment
    ): Boolean =
      if environment.countries.size > 0 then
        environment.countries.forall(country =>
          environment.interCountryRelations.countryEnemies(country.id).nonEmpty
        )
      else false
