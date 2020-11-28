package com.example.usecase

import com.example.`extension`.TxRunner
import com.example.domain.Order
import com.example.port.OrderPort
import javax.inject.{ Inject, Singleton }

import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class FetchOrderUseCase @Inject() (private val txRunner: TxRunner, private val orderPort: OrderPort) {

  def run(orderId: String)(implicit ec: ExecutionContext): Future[Option[Order]] =
    txRunner.runReadonly(implicit ctx => orderPort.findById(orderId))
}
