package com.example

import com.example.driver.ScalikeJdbcIOContext
import play.api.mvc.Result
import scalikejdbc.{ DB, ReadOnlyAutoSession }

import scala.concurrent.{ ExecutionContext, Future }

package object rest {

  type Recover = PartialFunction[Throwable, Future[Result]]

  object readOnly {
    def apply[A](f: ScalikeJdbcIOContext => Future[A]): Future[A] =
      f(ScalikeJdbcIOContext(ReadOnlyAutoSession))
  }

  object runTransactionally {
    def apply[A](f: ScalikeJdbcIOContext => Future[A])(implicit ec: ExecutionContext): Future[A] =
      DB futureLocalTx { session => f(ScalikeJdbcIOContext(session)) }
  }
}
