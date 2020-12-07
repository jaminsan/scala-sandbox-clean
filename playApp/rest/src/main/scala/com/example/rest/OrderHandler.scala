package com.example.rest

import com.example.usecase.CreateOrderUseCase.ItemStockNotFound
import com.example.usecase.{ CreateOrderUseCase, FetchOrderUseCase }
import javax.inject.{ Inject, Singleton }
import play.api.libs.json.{ Json, OWrites, Reads }
import play.api.mvc.{ AbstractController, ControllerComponents }

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.chaining.scalaUtilChainingOps

@Singleton
class OrderHandler @Inject() (
    private val cc:                 ControllerComponents,
    private val createOrderUseCase: CreateOrderUseCase,
    private val fetchOrderUseCase:  FetchOrderUseCase)(implicit ec: ExecutionContext)
    extends AbstractController(cc) {

  import OrderHandler._

  def postOrder() = Action.async(cc.parsers.json) { request =>
    val req = request.body.as[PostOrderRequest]

    val handleError: Recover = { case ItemStockNotFound(itemId) =>
      Future.successful(NotFound(s"Item not found itemId:$itemId"))
    }

    createOrderUseCase
      .run(req.customerId, req.itemId, req.quantity)
      .map { order =>
        PostOrderResponse(order.orderId)
          .pipe(Json.toJson(_))
          .pipe(Ok(_))
      }
      .recoverWith(handleError)
  }

  def getOrder(orderId: String) = Action.async {
    fetchOrderUseCase.run(orderId).map {
      case None =>
        NotFound(s"Order not found orderId:$orderId")

      case Some(order) =>
        GetOrderResponse(
          orderId = order.orderId,
          customerId = order.customerId,
          orderItem = GetOrderOrderItemResponse(itemId = order.orderItem.itemId, quantity = order.orderItem.quantity)
        )
          .pipe(Json.toJson(_))
          .pipe(Ok(_))
    }
  }
}

object OrderHandler {

  case class PostOrderRequest(customerId: String, itemId: String, quantity: Int)
  case class PostOrderResponse(orderId: String)
  implicit val reads:  Reads[PostOrderRequest]    = Json.reads[PostOrderRequest]
  implicit val writes: OWrites[PostOrderResponse] = Json.writes[PostOrderResponse]

  case class GetOrderResponse(orderId: String, customerId: String, orderItem: GetOrderOrderItemResponse)
  case class GetOrderOrderItemResponse(itemId: String, quantity: Int)
  implicit val getOrderOrderItemResponseWrites: OWrites[GetOrderOrderItemResponse] =
    Json.writes[GetOrderOrderItemResponse]
  implicit val getOrderResponseWrites:          OWrites[GetOrderResponse]          = Json.writes[GetOrderResponse]
}
