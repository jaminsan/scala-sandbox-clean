package com.example.usecase

import java.util.UUID

import cats.MonadError
import cats.implicits._
import com.example.domain.{ ItemStock, Order, OrderItem }
import com.example.port.{ ItemStockPort, OrderPort }
import com.example.usecase.CreateOrderUseCase.ItemStockNotFound

import scala.util.chaining.scalaUtilChainingOps

class CreateOrderUseCase[F[_]](private val itemStockPort: ItemStockPort[F], private val orderPort: OrderPort[F])(
    implicit ME:                                          MonadError[F, Throwable]) {

  def run(customerId: String, itemId: String, quantity: Int): F[Order] =
    for {
      itemStock  <- itemStockPort.findByItemId(itemId) getOrFailWith ItemStockNotFound(itemId)
      _          <- ItemStock.sub(itemStock, quantity) pipe itemStockPort.save
      savedOrder <- order(customerId, itemId, quantity) pipe orderPort.save
    } yield savedOrder

  private def order(customerId: String, itemId: String, quantity: Int): Order =
    Order(orderId = UUID.randomUUID().toString, customerId = customerId, orderItem = OrderItem(itemId, quantity))
}

object CreateOrderUseCase {

  case class ItemStockNotFound(itemId: String) extends RuntimeException(s"Item stock not found for item id $itemId")
}
