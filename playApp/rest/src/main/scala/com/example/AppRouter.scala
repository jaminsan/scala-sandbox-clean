package com.example

import com.example.rest.OrderHandler
import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class AppRouter @Inject() (private val orderHandler: OrderHandler) extends SimpleRouter {

  override def routes: Routes = {
    case GET(p"/orders/$orderId") => orderHandler.getOrder(orderId)

    case POST(p"/orders") => orderHandler.postOrder()
  }
}
