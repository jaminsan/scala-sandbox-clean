package com.example.usecase

import com.example.domain.Order
import com.example.port.{ IOContext, OrderPort }
import javax.inject.{ Inject, Singleton }

import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class FetchOrderUseCase @Inject() (private val orderPort: OrderPort) {

  def run(orderId: String)(implicit ctx: IOContext, ec: ExecutionContext): Future[Option[Order]] =
    orderPort.findById(orderId)
}
