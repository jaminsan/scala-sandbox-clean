package com.example.rest

import com.example.gateway.DbAccessExecutionContext
import com.example.rest.OrderHandler.{ GetOrderOrderItemResponse, GetOrderResponse, PostOrderRequest }
import com.example.usecase.CreateOrderUseCase.ItemStockNotFound
import javax.inject.{ Inject, Singleton }
import play.api.libs.json.Json
import play.api.mvc.{ AbstractController, ControllerComponents }

import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class OrderHandler @Inject() (
    private val cc:                       ControllerComponents,
    private val ec:                       ExecutionContext,
    private val dbAccessExecutionContext: DbAccessExecutionContext)
    extends AbstractController(cc) {

  // TODO: DI

  implicit val reads = Json.reads[PostOrderRequest]

  def postOrder() = Action.async(cc.parsers.json) { request =>
    val req = request.body.as[PostOrderRequest]

    val handleError: Recover = { case ItemStockNotFound(itemId) =>
      Future.successful(NotFound(s"Item not found itemId:$itemId"))
    }

    ???
  }

  implicit val getOrderOrderItemResponseWrites = Json.writes[GetOrderOrderItemResponse]
  implicit val getOrderResponseWrites          = Json.writes[GetOrderResponse]

  def getOrder(orderId: String) = Action.async {
    Future.successful(Ok(""))
  }
}

object OrderHandler {

  case class PostOrderRequest(customerId: String, itemId: String, quantity: Int)

  case class GetOrderResponse(orderId: String, customerId: String, orderItem: GetOrderOrderItemResponse)

  case class GetOrderOrderItemResponse(itemId: String, quantity: Int)
}
