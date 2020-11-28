package com.example.`extension`

import scala.concurrent.{ ExecutionContext, Future }

class NoTrasactionTxRunner extends TxRunner {

  override def runReadonly[A](f: IOContext => Future[A])(implicit ec: ExecutionContext): Future[A] = f(new IOContext {})

  override def run[A](f: IOContext => Future[A])(implicit ec: ExecutionContext): Future[A] = f(new IOContext {})
}
