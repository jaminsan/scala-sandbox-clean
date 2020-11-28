package com.example.driver

import com.example.`extension`.IOContext
import com.example.driver.ScalikeJdbcIOContext.toDBSession
import javax.inject.Singleton
import scalikejdbc._

import scala.concurrent.{ blocking, ExecutionContext, Future }

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

  def save(itemStockRecord: ItemStockRecord)(implicit ctx: IOContext, ec: ExecutionContext): Future[Int] =
    futureUpdate {
      update(ItemStockTable)
        .set(column.quantity -> itemStockRecord.quantity)
        .where
        .eq(column.itemId, itemStockRecord.itemId)
    }

  def findByItemId(itemId: String)(implicit ctx: IOContext, ec: ExecutionContext): Future[Option[ItemStockRecord]] =
    Future {
      blocking { // FIXME: ロック取得で競合が発生するので毎度別スレッドを生成する......ロック待ちになる場合は blocking した方が良さそうだな
        withSQL {
          selectFrom(ItemStockTable as is).where
            .eq(is.itemId, itemId).forUpdate
        }.map(ItemStockRecord(is)).single().apply()
      }
    }
}
