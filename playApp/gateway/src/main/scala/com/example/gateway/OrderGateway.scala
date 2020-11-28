package com.example.gateway

import com.example.`extension`.IOContext
import com.example.domain.{ Order, OrderItem }
import com.example.driver.{ OrderDao, OrderItemDao, OrderItemRecord, OrderRecord }
import com.example.port.OrderPort
import javax.inject.{ Inject, Singleton }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class OrderGateway @Inject() (private val orderDao: OrderDao, private val orderItemDao: OrderItemDao)
    extends OrderPort {

  override def save(order: Order)(implicit ctx: IOContext): Future[Order] =
    for {
      _ <- orderDao.insert(toOrderRecord(order))
      _ <- orderItemDao.insert(toOrderItemRecord(order.orderId, order.orderItem))
    } yield order

  override def findById(orderId: String)(implicit ctx: IOContext): Future[Option[Order]] =
    for {
      mayBeOrderRecord <- orderDao.findById(orderId)
      orderItemRecords <- orderItemDao.findByOrderId(orderId)
    } yield mayBeOrderRecord.map { o =>
      Order(
        orderId = o.orderId,
        customerId = o.customerId,
        orderItem = toOrderItem(orderItemRecords.head)
      )
    }

  private def toOrderRecord(order: Order): OrderRecord =
    OrderRecord(
      orderId = order.orderId,
      customerId = order.customerId
    )

  private def toOrderItemRecord(orderId: String, orderItem: OrderItem): OrderItemRecord =
    OrderItemRecord(
      orderId = orderId,
      itemId = orderItem.itemId,
      quantity = orderItem.quantity
    )

  private def toOrderItem(orderItemRecord: OrderItemRecord): OrderItem =
    OrderItem(
      itemId = orderItemRecord.itemId,
      quantity = orderItemRecord.quantity
    )
}
