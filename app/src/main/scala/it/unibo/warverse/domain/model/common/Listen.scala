package it.unibo.warverse.domain.model.common

import it.unibo.warverse.domain.model.common.Cancellable

object Listen:
  type ListenClosure[Event] = Event => Unit

  trait Listenable[Event]:
    private val listeners: List[ListenClosure[Event]] = List.empty
    def addListener(closure: ListenClosure[Event]): Cancellable =
      closure :: listeners
      Cancellable(() => removeListener(closure))
    def removeListener(closure: ListenClosure[Event]): Unit =
      listeners.filter(_ == closure)

    protected def emitEvent(event: Event): Unit =
      listeners.foreach(_(event))
