package com.example

import scala.concurrent.{ ExecutionContext, Future }

trait CheckoutCart[CustomerId, ShoppingCart, CatalogItem, Price, Payment, Order] {

  implicit val ec: ExecutionContext

  def fetchCart(customerId: CustomerId): Future[ShoppingCart]

  def calcPrice(shoppingCart: ShoppingCart): Future[Price]

  def fetchPayment(customerId: CustomerId): Future[Payment]

  def checkout(shoppingCart: ShoppingCart, price: Price, payment: Payment): Future[Order]

  def exec(customerId: CustomerId, payment: Option[Payment]): Future[Order] =
    for {
      cart  <- fetchCart(customerId)
      price <- calcPrice(cart)
      p     <- payment match {
        case Some(p) => Future.successful(p)
        case _ => fetchPayment(customerId)
      }
      order <- checkout(cart, price, p)
    } yield order
}
