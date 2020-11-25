package com.example.port

import com.example.domain.ItemStock

trait ItemStockPort[F[_]] {

  def findByItemId(itemId: String): F[Option[ItemStock]]

  def save(itemStock: ItemStock): F[ItemStock]
}
