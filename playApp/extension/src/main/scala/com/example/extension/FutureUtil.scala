package com.example.`extension`

import scala.concurrent.duration.{ Duration, SECONDS }
import scala.concurrent.{ Await, ExecutionContext, Future }

object FutureUtil {

  implicit class FutureOps[A](self: Future[A]) {
    def await(durationSeconds: Int = 10)(implicit ec: ExecutionContext): A =
      Await.result(self, Duration(durationSeconds, SECONDS))
  }
}
