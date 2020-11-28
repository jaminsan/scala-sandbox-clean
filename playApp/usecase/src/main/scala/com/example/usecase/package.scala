package com.example

import scala.concurrent.{ ExecutionContext, Future }

package object usecase {

  implicit class FutureOptionOps[A](private val self: Future[Option[A]]) {

    def getOrFailWith[E <: RuntimeException](e: E)(implicit ec: ExecutionContext): Future[A] =
      self.flatMap {
        case Some(v) => Future.successful(v)
        case None => Future.failed(e)
      }
  }

  implicit class FutureIdOps[T](private val obj: T) {

    def asFuture: Future[T] = Future.successful(obj)
  }
}
