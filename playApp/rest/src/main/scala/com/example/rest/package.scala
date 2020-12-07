package com.example

import play.api.mvc.Result

import scala.concurrent.Future

package object rest {

  type Recover = PartialFunction[Throwable, Future[Result]]
}
