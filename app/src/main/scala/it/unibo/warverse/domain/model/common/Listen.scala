package it.unibo.warverse.domain.model.common

import it.unibo.warverse.domain.model.common.Disposable
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

object Listen:
  type ListenClosure[Event] = Event => Unit

  /** An object that can emit one or more event that can be listened to
    * @tparam Event
    *   the type of the events
    */
  trait Listenable[Event]:
    /** Adds a listener to the [[Listenable]]
      * @param closure
      *   the closure that gets executed on each event
      * @return
      *   a [[Disposable]] to stop listening for new events
      */
    def addListener(closure: ListenClosure[Event]): Disposable

    /** Removes a listener from the [[Listenable]]
      *
      * @param closure
      *   the closure that was getting executed on each event
      */
    def removeListener(closure: ListenClosure[Event]): Unit
    protected def emitEvent(event: Event): Unit

  /** An abstract class to make the implementation of [[Listenable]] easier
    * @tparam Event
    *   the type of the events
    */
  abstract class SimpleListenable[Event] extends Listenable[Event]:
    private var listeners: List[ListenClosure[Event]] = List.empty

    /** Adds a listener to the [[Listenable]]
      *
      * @param closure
      *   the closure that gets executed on each event
      * @return
      *   a [[Disposable]] to stop listening for new events
      */
    def addListener(closure: ListenClosure[Event]): Disposable =
      listeners = closure :: listeners
      Disposable(() => removeListener(closure))

    /** Removes a listener from the [[Listenable]]
      *
      * @param closure
      *   the closure that was getting executed on each event
      */
    def removeListener(closure: ListenClosure[Event]): Unit =
      listeners = listeners.filter(_ != closure)

    protected def emitEvent(event: Event): Unit =
      listeners.foreach(listener =>
        Task {
          listener(event)
        }.runToFuture
      )

  /** Utility class to create idiomatic listeners
    * @tparam Event
    *   type of event emitted by [[Listenable]]
    */
  case class OnEventPart[Event]():
    def from(listenable: Listenable[Event]): OnEventWithListener[Event] =
      OnEventWithListener(listenable)

  /** Utility class to create idiomatic listeners
    * @param listenable
    *   the object to listen to
    * @tparam Event
    *   type of event emitted by [[Listenable]]
    */
  case class OnEventWithListener[Event](
    listenable: Listenable[Event]
  ):
    /** It runs the given closure on each event emitted by `listenable`
      * @param closure
      *   the closure to invoke on each event
      * @return
      *   a [[Disposable]] to stop listening for new events
      */
    def run(closure: ListenClosure[Event]): Disposable =
      listenable.addListener(closure)

  /** Utility method to listen to events in a more idiomatic way
    * @tparam Event
    *   type of event emitted by [[Listenable]]
    * @return
    *   a [[Disposable]] to stop listening for new events
    */
  def onReceiveEvent[Event]: OnEventPart[Event] =
    OnEventPart()
