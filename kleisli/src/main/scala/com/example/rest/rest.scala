package com.example

import play.api.mvc.Result
import scalikejdbc.{ DB, DBSession, ReadOnlyAutoSession }

import scala.concurrent.{ ExecutionContext, Future }

package object rest {

  type Recover = PartialFunction[Throwable, Future[Result]]

  object readOnly {
    def apply[A](f: DBSession => Future[A]): Future[A] =
      f(ReadOnlyAutoSession)
  }

  object runTransactionally {
    def apply[A](f: DBSession => Future[A])(implicit ec: ExecutionContext): Future[A] =
      DB futureLocalTx { session => f(session) }
  }
}
