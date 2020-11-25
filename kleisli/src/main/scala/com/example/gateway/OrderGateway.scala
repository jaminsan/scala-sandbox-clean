package com.example.gateway

import cats.data.Kleisli
import com.example.domain.{ Order, OrderItem }
import com.example.port.OrderPort

import scala.concurrent.{ ExecutionContext, Future }
import scalikejdbc._

class OrderGateway()(implicit ec: ExecutionContext) extends OrderPort[DBIO] {

  override def save(order: Order): DBIO[Order] =
    Kleisli { implicit session =>
      Future {
        sql"""insert into order (order_id, customer_id) values ({orderId}, {customerId})"""
          .bindByName(Symbol("orderId") -> order.orderId, Symbol("customerId") -> order.customerId)
          .update()
          .apply()

        order
      }
    }

  override def findById(orderId: String): DBIO[Option[Order]] =
    Kleisli { implicit session =>
      Future {
        val orderItem = sql"""select * from order_item where order_id = ${orderId}"""
          .map(rs => OrderItem(rs.string(1), rs.int(2)))
          .list()
          .apply()
          .head

        sql"""select * from order where order_id = ${orderId}"""
          .map(rs => Order(rs.string(1), rs.string(2), orderItem))
          .single()
          .apply()
      }
    }
}
