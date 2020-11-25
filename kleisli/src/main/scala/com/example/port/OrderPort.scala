package com.example.port

import com.example.domain.Order

trait OrderPort[F[_]] {

  def save(order: Order): F[Order]

  def findById(orderId: String): F[Option[Order]]
}
