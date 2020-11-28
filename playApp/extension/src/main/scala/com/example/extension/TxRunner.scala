package com.example.`extension`

import scala.concurrent.{ ExecutionContext, Future }

trait TxRunner {

  def runReadonly[A](f: IOContext => Future[A])(implicit ec: ExecutionContext): Future[A]

  def run[A](f: IOContext => Future[A])(implicit ex: ExecutionContext): Future[A]
}
