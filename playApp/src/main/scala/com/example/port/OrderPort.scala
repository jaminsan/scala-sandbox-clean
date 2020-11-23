package com.example.port

import com.example.domain.Order

import scala.concurrent.Future

trait OrderPort {

  def save(order: Order)(implicit ctx: IOContext): Future[Order]

  def findById(orderId: String)(implicit ctx: IOContext): Future[Option[Order]]
}
