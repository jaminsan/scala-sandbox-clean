package com.example.gateway

import cats.data.Kleisli
import com.example.domain.ItemStock
import com.example.port.ItemStockPort
import scalikejdbc._

import scala.concurrent.{ ExecutionContext, Future }

class ItemStockGateway()(implicit ec: ExecutionContext) extends ItemStockPort[DBIO] {

  override def findByItemId(itemId: String): DBIO[Option[ItemStock]] =
    Kleisli { implicit session =>
      Future {
        sql"""select * from item_stock where item_id = $itemId"""
          .map(rs => ItemStock(rs.string(1), rs.int(2)))
          .single()
          .apply()
      }
    }

  override def save(itemStock: ItemStock): DBIO[ItemStock] =
    Kleisli { implicit session =>
      Future {
        sql"""update item_stock set quantity = ${itemStock.quantity} where item_id = ${itemStock.itemId}"""
          .update()
          .apply()

        itemStock
      }
    }
}
