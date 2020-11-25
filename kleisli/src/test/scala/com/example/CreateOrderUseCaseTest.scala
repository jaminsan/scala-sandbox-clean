package com.example

import cats.implicits.catsStdInstancesForFuture
import com.example.domain.{ ItemStock, Order }
import com.example.port.{ ItemStockPort, OrderPort }
import com.example.usecase.CreateOrderUseCase
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }

class CreateOrderUseCaseTest extends AnyFlatSpec with MockFactory with Matchers {

  behavior of "run"

  it should "hoge" in new withMock {
    (itemStockPortMock.findByItemId _)
      .expects("itemId")
      .returns(Future.successful(Some(ItemStock("itemId", 1))))
      .once()

    (itemStockPortMock.save _)
      .expects(ItemStock("itemId", 0))
      .onCall((itemStock: ItemStock) => Future.successful(itemStock))
      .once()

    (orderPortMock.save _)
      .expects(where { order: Order =>
        order.customerId == "customerId" &&
        order.orderItem.itemId == "itemId" &&
        order.orderItem.quantity == 1
      })
      .onCall((order: Order) => Future.successful(order))
      .once()

    val result = Await.result(sut.run("customerId", "itemId", 1), Duration("10s"))
    result.customerId shouldBe "customerId"
    result.orderItem.itemId shouldBe "itemId"
    result.orderItem.quantity shouldBe 1
  }

  trait withMock {
    val itemStockPortMock = mock[ItemStockPort[Future]]
    val orderPortMock     = mock[OrderPort[Future]]
    val sut               = new CreateOrderUseCase[Future](itemStockPortMock, orderPortMock)
  }
}
