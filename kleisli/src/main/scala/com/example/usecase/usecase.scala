package com.example

import cats.MonadError
import cats.implicits._

import scala.concurrent.ExecutionContext

package object usecase {

  implicit class MonadErrorOptionOps[F[_], A](private val self: F[Option[A]])(implicit ME: MonadError[F, Throwable]) {

    def getOrFailWith[E <: RuntimeException](e: E): F[A] =
      self.flatMap {
        case None => ME.raiseError(e)
        case Some(v) => ME.pure(v)
      }
  }
}
