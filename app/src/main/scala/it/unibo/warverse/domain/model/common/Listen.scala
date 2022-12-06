package it.unibo.warverse.domain.model.common

import it.unibo.warverse.domain.model.common.Disposable

object Listen:
  type ListenClosure[Event] = Event => Unit

  trait Listenable[Event]:
    def addListener(closure: ListenClosure[Event]): Disposable
    def removeListener(closure: ListenClosure[Event]): Unit
    protected def emitEvent(event: Event): Unit

  abstract class SimpleListenable[Event] extends Listenable[Event]:
    private var listeners: List[ListenClosure[Event]] = List.empty

    def addListener(closure: ListenClosure[Event]): Disposable =
      listeners = closure :: listeners
      Disposable(() => removeListener(closure))

    def removeListener(closure: ListenClosure[Event]): Unit =
      listeners = listeners.filter(_ == closure)

    protected def emitEvent(event: Event): Unit =
      listeners.foreach(_(event))

  case class OnEventPart[Event]():
    def from(listenable: Listenable[Event]): OnEventWithListener[Event] =
      OnEventWithListener(listenable)
  case class OnEventWithListener[Event](
    listenable: Listenable[Event]
  ):
    def run(closure: ListenClosure[Event]): Disposable =
      listenable.addListener(closure)

  def onReceiveEvent[Event]: OnEventPart[Event] =
    OnEventPart()
