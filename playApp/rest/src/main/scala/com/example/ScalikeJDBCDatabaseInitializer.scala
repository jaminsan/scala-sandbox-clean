package com.example

import com.typesafe.config.Config
import javax.inject.{ Inject, Singleton }
import play.api.Configuration
import play.api.db.DBApi
import play.api.inject.ApplicationLifecycle
import scalikejdbc.config.{ DBs, TypesafeConfig, TypesafeConfigReader }
import scalikejdbc.{ ConnectionPool, DataSourceConnectionPool, GlobalSettings }

import scala.concurrent.Future

@Singleton
class ScalikeJDBCDatabaseInitializer @Inject() (
    dBApi:         DBApi,
    configuration: Configuration,
    lifecycle:     ApplicationLifecycle) {

  private lazy val DBs = new DBs with TypesafeConfigReader with TypesafeConfig {
    override val config: Config = configuration.underlying
  }

  def onStart(): Unit = {
    DBs.loadGlobalSettings()
    GlobalSettings.loggingSQLErrors = true
    dBApi.databases().foreach(db => ConnectionPool.singleton(new DataSourceConnectionPool(db.dataSource)))
  }

  def onStop(): Unit = {}

  lifecycle.addStopHook(() => Future.successful(onStop()))

  onStart()
}
