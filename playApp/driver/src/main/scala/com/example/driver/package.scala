package com.example

import scalikejdbc.{ DBSession, _ }

import scala.concurrent.{ ExecutionContext, Future }

package object driver {

  object futureUpdate {

    def apply[A <: SQLBuilder[UpdateOperation]](builder: A)(implicit s: DBSession, ec: ExecutionContext): Future[Int] =
      Future {
        withSQL[UpdateOperation](builder).update().apply()
      }
  }
}
