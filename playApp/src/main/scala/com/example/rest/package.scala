package com.example

import com.example.gateway.ScalikeJdbcContext
import play.api.mvc.Result
import scalikejdbc.{ DB, ReadOnlyAutoSession }

import scala.concurrent.{ ExecutionContext, Future }

package object rest {

  type Recover = PartialFunction[Throwable, Future[Result]]

  object readOnly {
    def apply[A](f: ScalikeJdbcContext => Future[A]): Future[A] =
      f(ScalikeJdbcContext(ReadOnlyAutoSession))
  }

  object runTransactionally {
    def apply[A](f: ScalikeJdbcContext => Future[A])(implicit ec: ExecutionContext): Future[A] =
      DB futureLocalTx { session => f(ScalikeJdbcContext(session)) }
  }
}
