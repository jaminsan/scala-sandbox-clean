package com.example.usecase

import com.example.TestUtil.FutureOps
import com.example.domain.{ ItemStock, Order }
import com.example.port.{ IOContext, ItemStockPort, OrderPort }
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CreateOrderUseCaseTest extends AnyFlatSpec with MockFactory {
  implicit val ctx: IOContext = new IOContext {}

  behavior of "run"

  it should "success" in new withMock {
    (itemStockPort
      .findByItemId(_: String)(_: IOContext))
      .expects("itemId", ctx)
      .returns(Future.successful(Some(ItemStock("itemId", 10))))
      .once()

    (itemStockPort
      .save(_: ItemStock)(_: IOContext))
      .expects(ItemStock("itemId", 9), ctx)
      .onCall((itemStock, _) => Future.successful(itemStock))
      .once()

    (orderPort
      .save(_: Order)(_: IOContext))
      .expects(where { (order, _) =>
        order.customerId == "customerId" &&
        order.orderItem.itemId == "itemId" &&
        order.orderItem.quantity == 1
      })
      .onCall((order, _) => Future.successful(order))
      .once()

    sut.run("customerId", "itemId", 1).await()
  }

  trait withMock {
    val itemStockPort = mock[ItemStockPort]
    val orderPort     = mock[OrderPort]
    val sut           = new CreateOrderUseCase(itemStockPort, orderPort)
  }
}
