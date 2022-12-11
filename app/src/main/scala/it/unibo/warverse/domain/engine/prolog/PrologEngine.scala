package it.unibo.warverse.domain.engine.prolog

import scala.jdk.CollectionConverters.*
import alice.tuprolog.{Prolog, SolveInfo, Struct, Term, Theory, Var}

import scala.collection.immutable.Seq
import scala.reflect.ClassTag
import scala.util.Try

trait PrologEngine:
  def getResultTerms(goal: Term): LazyList[Term]
  def getResultVars(goal: Term): LazyList[Seq[Var]]

object PrologEngine:
  given Conversion[String, Term] = Term.createTerm(_)

  def apply(): PrologEngine = PrologEngineImpl()

  class PrologEngineImpl extends PrologEngine:
    private val engine = new Prolog()
    engine.setTheory(
      Theory(
        ClassLoader
          .getSystemResource("prolog_theory.pl")
          .openStream()
      )
    )
    override def getResultTerms(goal: Term): LazyList[Term] =
      IterableTerms(engine, goal).to(LazyList)

    override def getResultVars(goal: Term): LazyList[Seq[Var]] =
      IterableVars(engine, goal).to(LazyList)
  private class IterableTerms(engine: Prolog, goal: Term)
      extends Iterable[Term]:
    override def iterator: Iterator[Term] = new Iterator[Term]:
      var advanceFailed = false
      var solution: SolveInfo = engine.solve(goal)

      override def hasNext: Boolean =
        (solution.isSuccess || solution.hasOpenAlternatives) && !advanceFailed

      override def next(): Term =
        val currentResult = solution.getSolution
        if solution.hasOpenAlternatives then solution = engine.solveNext()
        else advanceFailed = true
        currentResult

  private class IterableVars(engine: Prolog, goal: Term)
      extends Iterable[Seq[Var]]:
    override def iterator: Iterator[Seq[Var]] = new Iterator[Seq[Var]]:
      var advanceFailed = false
      var solution: SolveInfo = engine.solve(goal)

      override def hasNext: Boolean =
        (solution.isSuccess || solution.hasOpenAlternatives) && !advanceFailed

      override def next(): Seq[Var] =
        val currentResult = solution.getBindingVars
        if solution.hasOpenAlternatives then solution = engine.solveNext()
        else advanceFailed = true
        currentResult.asScala.toSeq

  extension (field: Seq[?])
    def toPrologList: String =
      field.mkString("[", ",", "]")
