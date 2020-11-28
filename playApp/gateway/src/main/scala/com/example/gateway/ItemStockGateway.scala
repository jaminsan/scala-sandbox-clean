package com.example.gateway

import com.example.`extension`.IOContext
import com.example.domain.ItemStock
import com.example.driver.{ ItemStockDao, ItemStockRecord }
import com.example.port.ItemStockPort
import javax.inject.{ Inject, Singleton }

import scala.concurrent.Future
import scala.util.chaining.scalaUtilChainingOps
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ItemStockGateway @Inject() (private val itemStockDao: ItemStockDao) extends ItemStockPort {

  override def save(itemStock: ItemStock)(implicit ctx: IOContext): Future[ItemStock] =
    toItemStockRecord(itemStock) pipe itemStockDao.save map (_ => itemStock)

  override def findByItemId(itemId: String)(implicit ctx: IOContext): Future[Option[ItemStock]] =
    for {
      mayBeItemStockRecord <- itemStockDao.findByItemId(itemId)
    } yield mayBeItemStockRecord.map(toItemStock)

  private def toItemStockRecord(itemStock: ItemStock): ItemStockRecord =
    ItemStockRecord(
      itemId = itemStock.itemId,
      quantity = itemStock.quantity
    )

  private def toItemStock(itemStockRecord: ItemStockRecord): ItemStock =
    ItemStock(
      itemId = itemStockRecord.itemId,
      quantity = itemStockRecord.quantity
    )
}
