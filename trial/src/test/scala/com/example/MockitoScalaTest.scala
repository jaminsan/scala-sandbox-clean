package com.example

import com.example.MockitoScalaTest.{ ItemId, Order, OrderId, Orders }
import org.mockito.{ ArgumentMatchersSugar, MockitoSugar }
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.util.chaining.scalaUtilChainingOps

class MockitoScalaTest extends AnyWordSpec with Matchers with MockitoSugar with ArgumentMatchersSugar {

  "mock" should {

    "success for Class extends Iterable" in {
      val orders = mock[Orders]

      when(orders.canShip).thenReturn(true)
      orders.canShip shouldBe true

      val ordersExceptCancel = mock[Orders]
      when(orders.cancel(eqTo(OrderId("orderId")))) thenReturn ordersExceptCancel
      orders.cancel(OrderId("orderId")) shouldBe ordersExceptCancel
      orders.cancel(OrderId("orderId")) shouldNot be(orders)
    }
  }

  "spy" should {

    "success for Class extends Iterable" in {
      val order1 = Order(OrderId("orderId1"), ItemId("itemId"), false)
      val order2 = Order(OrderId("orderId2"), ItemId("itemId"), true)
      val orders = List(order1, order2) pipe Orders

      val ordersSpy = spy(orders)

      orders.canShip shouldBe false
      when(ordersSpy.canShip).thenReturn(true)
      ordersSpy.canShip shouldBe true

      orders.cancel(OrderId("orderId1")) shouldBe List(order2).pipe(Orders)
      when(ordersSpy.cancel(eqTo(OrderId("orderId1")))) thenReturn Orders(Nil)
      ordersSpy.cancel(OrderId("orderId1")) shouldBe Orders(Nil)
    }
  }
}

object MockitoScalaTest {

  trait MyCollection[A] extends Iterable[A] {
    def list: List[A]

    override def iterator: Iterator[A] = list.iterator
  }

  case class OrderId(v: String) extends AnyVal
  case class ItemId(v: String) extends AnyVal
  case class Order(orderId: OrderId, itemId: ItemId, paid: Boolean)

  case class Orders(list: List[Order]) extends MyCollection[Order] {
    def canShip: Boolean = list.forall(_.paid)

    def cancel(orderId: OrderId): Orders = list.filterNot(_.orderId == orderId) pipe Orders
  }
}
