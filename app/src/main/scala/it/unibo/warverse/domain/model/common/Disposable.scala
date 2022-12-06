package it.unibo.warverse.domain.model.common

case class Disposable(dispose: () => Unit)
