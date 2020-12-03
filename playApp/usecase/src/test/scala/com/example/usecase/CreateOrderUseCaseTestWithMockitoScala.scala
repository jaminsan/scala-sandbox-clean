package com.example.usecase

import com.example.`extension`.FutureUtil.FutureOps
import com.example.`extension`.{ IOContext, NoTransactionTxRunner }
import com.example.domain.{ ItemStock, Order }
import com.example.port.{ ItemStockPort, OrderPort }
import org.mockito.captor.ArgCaptor
import org.mockito.{ ArgumentMatchersSugar, MockitoSugar }
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CreateOrderUseCaseTestWithMockitoScala
    extends AnyFlatSpec
    with Matchers
    with MockitoSugar
    with ArgumentMatchersSugar {

  behavior of "run"

  it should "success" in new withMock {

    when(itemStockPort.findByItemId(eqTo("itemId"))(*))
      .thenReturn(Future.successful(Some(ItemStock("itemId", 10))))

    when(itemStockPort.save(eqTo(ItemStock("itemId", 9)))(*))
      .thenAnswer((is: ItemStock, _: IOContext) => Future.successful(is))

    when(orderPort.save(any[Order])(any[IOContext]))
      .thenAnswer((o: Order, _: IOContext) => Future.successful(o))

    sut.run("customerId", "itemId", 1).await()

    val actualOrder = ArgCaptor[Order]
    verify(orderPort).save(actualOrder)(*)

    actualOrder.value.customerId shouldBe "customerId"
    actualOrder.value.orderItem.itemId shouldBe "itemId"
    actualOrder.value.orderItem.quantity shouldBe 1
  }

  trait withMock {
    val itemStockPort = mock[ItemStockPort]
    val orderPort     = mock[OrderPort]

    val sut = new CreateOrderUseCase(new NoTransactionTxRunner, itemStockPort, orderPort)
  }
}
