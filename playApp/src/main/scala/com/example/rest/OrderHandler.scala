package com.example.rest

import com.example.rest.OrderHandler.{ GetOrderOrderItemResponse, GetOrderResponse, PostOrderRequest }
import com.example.usecase.CreateOrderUseCase.ItemStockNotFound
import com.example.usecase.{ CreateOrderUseCase, FetchOrderUseCase }
import javax.inject.{ Inject, Singleton }
import play.api.libs.json.Json
import play.api.mvc.{ AbstractController, ControllerComponents }

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.chaining.scalaUtilChainingOps

@Singleton
class OrderHandler @Inject() (
    private val cc:                 ControllerComponents,
    private val createOrderUseCase: CreateOrderUseCase,
    private val fetchOrderUseCase:  FetchOrderUseCase)(implicit ec: ExecutionContext)
    extends AbstractController(cc) {

  implicit val reads = Json.reads[PostOrderRequest]

  def postOrder() = Action.async(cc.parsers.json) { request =>
    val req = request.body.as[PostOrderRequest]

    val handleError: Recover = { case ItemStockNotFound(itemId) =>
      Future.successful(NotFound(s"Item not found itemId:$itemId"))
    }

    runTransactionally(implicit ctx => createOrderUseCase.run(req.customerId, req.itemId, req.quantity))
      .map(order => Ok(order.orderId))
      .recoverWith(handleError)
  }

  implicit val getOrderOrderItemResponseWrites = Json.writes[GetOrderOrderItemResponse]
  implicit val getOrderResponseWrites          = Json.writes[GetOrderResponse]

  def getOrder(orderId: String) = Action.async {
    readOnly { implicit ctx =>
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
}

object OrderHandler {

  case class PostOrderRequest(customerId: String, itemId: String, quantity: Int)

  case class GetOrderResponse(orderId: String, customerId: String, orderItem: GetOrderOrderItemResponse)

  case class GetOrderOrderItemResponse(itemId: String, quantity: Int)
}
