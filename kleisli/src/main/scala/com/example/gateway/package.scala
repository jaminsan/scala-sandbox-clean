package com.example

import cats.data.Kleisli
import scalikejdbc.DBSession

import scala.concurrent.Future

package object gateway {

  type DBIO[A] = Kleisli[Future, DBSession, A]
}
