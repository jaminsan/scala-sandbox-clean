package com.example.driver

import com.example.`extension`.IOContext
import com.example.driver.ScalikeJdbcIOContext.toDBSession
import javax.inject.Singleton
import scalikejdbc._

import scala.concurrent.{ ExecutionContext, Future }

case class OrderItemRecord(orderId: String, itemId: String, quantity: Int)

object OrderItemRecord {

  def apply(oi: SyntaxProvider[OrderItemRecord])(ws: WrappedResultSet): OrderItemRecord =
    apply(oi.resultName)(ws)

  def apply(oi: ResultName[OrderItemRecord])(ws: WrappedResultSet): OrderItemRecord =
    OrderItemRecord(
      orderId = ws.string(oi.orderId),
      itemId = ws.string(oi.itemId),
      quantity = ws.int(oi.quantity)
    )
}

object OrderItemTable extends SQLSyntaxSupport[OrderItemRecord] {

  override def tableName: String = "order_item"

  val oi = syntax("oi")
}

@Singleton
class OrderItemDao {

  import OrderItemTable._

  def insert(orderItemRecord: OrderItemRecord)(implicit ctx: IOContext, ec: ExecutionContext): Future[Int] =
    futureUpdate {
      insertInto(OrderItemTable)
        .namedValues(
          column.orderId  -> orderItemRecord.orderId,
          column.itemId   -> orderItemRecord.itemId,
          column.quantity -> orderItemRecord.quantity
        )
    }

  def findByOrderId(orderId: String)(implicit ctx: IOContext, ec: ExecutionContext): Future[List[OrderItemRecord]] =
    Future {
      withSQL {
        selectFrom(OrderItemTable as oi).where
          .eq(oi.orderId, orderId)
      }.map(OrderItemRecord(oi)).list().apply()
    }
}
