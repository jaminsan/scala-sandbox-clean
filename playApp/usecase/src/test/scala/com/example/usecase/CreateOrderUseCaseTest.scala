package com.example.usecase

import com.example.`extension`.FutureUtil.FutureOps
import com.example.`extension`.{ IOContext, NoTransactionTxRunner }
import com.example.domain.{ ItemStock, Order }
import com.example.port.{ ItemStockPort, OrderPort }
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CreateOrderUseCaseTest extends AnyFlatSpec with MockFactory {

  behavior of "run"

  it should "success" in new withMock {
    (itemStockPort
      .findByItemId(_: String)(_: IOContext))
      .expects("itemId", *)
      .returns(Future.successful(Some(ItemStock("itemId", 10))))
      .once()

    (itemStockPort
      .save(_: ItemStock)(_: IOContext))
      .expects(ItemStock("itemId", 9), *)
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
    val sut           = new CreateOrderUseCase(new NoTransactionTxRunner, itemStockPort, orderPort)
  }
}
