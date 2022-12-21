package it.unibo.warverse.domain.model.common

/** The Disposable interface is used for life-cycle management of resources
  * @param dispose
  *   it is called when resource should be disposed
  */
case class Disposable(dispose: () => Unit)

extension (field: Iterable[Disposable])
  def dispose(): Unit =
    field.foreach(_.dispose)
