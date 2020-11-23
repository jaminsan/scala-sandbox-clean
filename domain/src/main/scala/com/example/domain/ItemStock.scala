package com.example.domain

case class ItemStock(itemId: String, quantity: Int)

object ItemStock {

  def sub(itemStock: ItemStock, quantity: Int): ItemStock =
    itemStock.copy(quantity = itemStock.quantity - quantity)
}
