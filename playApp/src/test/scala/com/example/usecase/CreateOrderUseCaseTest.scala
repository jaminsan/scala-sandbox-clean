package com.example.usecase

import com.example.domain.{ ItemStock, Order }
import com.example.port.{ IOContext, ItemStockPort, OrderPort }
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.{ AnyFlatSpec, AsyncFlatSpec }

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }

class CreateOrderUseCaseTest extends AnyFlatSpec with MockFactory {

  behavior of "run"

  implicit val ctx: IOContext = new IOContext {}

  it should "success" ignore {

    val sut = new withMock {
      (itemStockPort
        .findByItemId(_: String)(_: IOContext))
        .expects("itemId", ctx)
        .returns(Future.successful(Some(ItemStock("itemId", 10))))
        .once()

      (itemStockPort
        .save(_: ItemStock)(_: IOContext))
        .expects(ItemStock("itemId", 9), ctx)
        .returns(Future.successful(ItemStock("itemId", 9)))
        .once()

      (orderPort
        .save(_: Order)(_: IOContext))
        .expects(where { (order, _) =>
          order.customerId == "customerId" &&
          order.orderItem.itemId == "itemId" &&
          order.orderItem.quantity == 2
        })
        .once()
    }.createOrderUseCase

    import scala.concurrent.ExecutionContext.Implicits.global
    Await.result(sut.run("customerId", "itemId", 1), Duration("30s"))

//    sut.run("customerId", "itemId", 1) map { order =>
//      assert(order.customerId == "customerId")
//      assert(order.orderItem.itemId == "itemId")
//      assert(order.orderItem.quantity == 1)
//    }
  }

  trait withMock {
    val itemStockPort      = mock[ItemStockPort]
    val orderPort          = mock[OrderPort]
    val createOrderUseCase = new CreateOrderUseCase(itemStockPort, orderPort)
  }
}
