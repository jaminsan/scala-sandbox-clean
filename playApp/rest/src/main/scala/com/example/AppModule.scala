package com.example

import com.example.`extension`.TxRunner
import com.example.driver.ScalikeJDBCTxRunner
import com.example.gateway.{ ItemStockGateway, OrderGateway }
import com.example.port.{ ItemStockPort, OrderPort }
import com.google.inject.AbstractModule

class AppModule extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[TxRunner]).to(classOf[ScalikeJDBCTxRunner])
    bind(classOf[ItemStockPort]).to(classOf[ItemStockGateway])
    bind(classOf[OrderPort]).to(classOf[OrderGateway])
    bind(classOf[ScalikeJDBCDatabaseInitializer]).asEagerSingleton()
  }
}
