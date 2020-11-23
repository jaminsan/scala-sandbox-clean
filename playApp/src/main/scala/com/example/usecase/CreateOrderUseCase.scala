package com.example.usecase

import java.util.UUID

import com.example.domain.{ ItemStock, Order, OrderItem }
import com.example.port.{ IOContext, ItemStockPort, OrderPort }
import com.example.usecase.CreateOrderUseCase.{ ItemStockNotFound, OrderId }
import javax.inject.{ Inject, Singleton }

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.chaining.scalaUtilChainingOps

@Singleton
class CreateOrderUseCase @Inject() (itemStockPort: ItemStockPort, orderPort: OrderPort) {

  def run(customerId: String, itemId:    String, quantity: Int)(implicit
      ctx:            IOContext,
      ec:             ExecutionContext): Future[Order]    =
    for {
      itemStock  <- itemStockPort.findByItemId(itemId) getOrFailWith ItemStockNotFound(itemId)
      _          <- ItemStock.sub(itemStock, quantity) pipe itemStockPort.save
      savedOrder <- order(customerId, itemId, quantity) pipe orderPort.save
    } yield savedOrder

  private def order(customerId: String, itemId: String, quantity: Int): Order =
    Order(orderId = UUID.randomUUID().toString, customerId = customerId, orderItem = OrderItem(itemId, quantity))
}

object CreateOrderUseCase {

  type OrderId = String

  case class ItemStockNotFound(itemId: String) extends RuntimeException(s"Item stock not found for item id $itemId")
}
