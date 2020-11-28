package com.example.driver

import com.example.`extension`.IOContext
import com.example.driver.ScalikeJdbcIOContext.toDBSession
import javax.inject.Singleton
import scalikejdbc._

import scala.concurrent.{ ExecutionContext, Future }

case class OrderRecord(orderId: String, customerId: String)

object OrderRecord {

  def apply(o: SyntaxProvider[OrderRecord])(ws: WrappedResultSet): OrderRecord =
    apply(o.resultName)(ws)

  def apply(o: ResultName[OrderRecord])(ws: WrappedResultSet): OrderRecord =
    OrderRecord(
      orderId = ws.string(o.orderId),
      customerId = ws.string(o.customerId)
    )
}

object OrderTable extends SQLSyntaxSupport[OrderRecord] {

  override def tableName: String = "order_"

  val o = syntax("o")
}

@Singleton
class OrderDao {

  import OrderTable._

  def insert(orderRecord: OrderRecord)(implicit ctx: IOContext, ec: ExecutionContext): Future[Int] =
    futureUpdate {
      insertInto(OrderTable)
        .namedValues(
          column.orderId    -> orderRecord.orderId,
          column.customerId -> orderRecord.customerId
        )
    }

  def findById(orderId: String)(implicit ctx: IOContext, ec: ExecutionContext): Future[Option[OrderRecord]] =
    Future {
      withSQL {
        selectFrom(OrderTable as o).where
          .eq(o.orderId, orderId)
      }.map(OrderRecord(o)).single().apply()
    }
}
