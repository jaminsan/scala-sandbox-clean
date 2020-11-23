package com.example

import com.example.gateway.{ DbAccessExecutionContext, ItemStockGateway, OrderGateway }
import com.example.port.{ ItemStockPort, OrderPort }
import com.google.inject.AbstractModule

class AppModule extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[ItemStockPort]).to(classOf[ItemStockGateway])
    bind(classOf[OrderPort]).to(classOf[OrderGateway])
    bind(classOf[DbAccessExecutionContext]).to(classOf[DbAccessExecutionContextImpl]).asEagerSingleton()
  }
}
