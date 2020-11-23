package com.example.driver

import javax.inject.Singleton
import scalikejdbc._

import scala.concurrent.{ ExecutionContext, Future }

case class ItemStockRecord(
    itemId:   String,
    quantity: Int
)

object ItemStockRecord {

  def apply(is: SyntaxProvider[ItemStockRecord])(ws: WrappedResultSet): ItemStockRecord =
    apply(is.resultName)(ws)

  def apply(is: ResultName[ItemStockRecord])(ws: WrappedResultSet): ItemStockRecord =
    ItemStockRecord(
      itemId = ws.string(is.itemId),
      quantity = ws.int(is.quantity)
    )
}

object ItemStockTable extends SQLSyntaxSupport[ItemStockRecord] {

  override def tableName: String = "item_stock"

  val is = syntax("is_")
}

@Singleton
class ItemStockDao {

  import ItemStockTable._

  def save(itemStockRecord: ItemStockRecord)(implicit session: DBSession, ec: ExecutionContext): Future[Int] =
    futureUpdate {
      update(ItemStockTable)
        .set(column.quantity -> itemStockRecord.quantity)
        .where
        .eq(column.itemId, itemStockRecord.itemId)
    }

  def findByItemId(itemId: String)(implicit session: DBSession, ec: ExecutionContext): Future[Option[ItemStockRecord]] =
    Future {
      withSQL {
        selectFrom(ItemStockTable as is).where
          .eq(is.itemId, itemId)
      }.map(ItemStockRecord(is)).single().apply()
    }
}
