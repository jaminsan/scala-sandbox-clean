package com.example.port

import com.example.`extension`.IOContext
import com.example.domain.ItemStock

import scala.concurrent.Future

trait ItemStockPort {

  def save(itemStock: ItemStock)(implicit ctx: IOContext): Future[ItemStock]

  def findByItemId(itemId: String)(implicit ctx: IOContext): Future[Option[ItemStock]]
}
