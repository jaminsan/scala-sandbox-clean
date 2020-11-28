package com.example.driver

import akka.actor.ActorSystem
import com.example.`extension`.{ IOContext, TxRunner }
import javax.inject.{ Inject, Singleton }
import scalikejdbc._

import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class ScalikeJDBCTxRunner @Inject() (actorSystem: ActorSystem) extends TxRunner {

  // FIXME: connection 取得は blocking する処理なので executionContext 分けた方が良さそうなのかな？
  override def runReadonly[A](f: IOContext => Future[A])(implicit ec: ExecutionContext): Future[A] =
    getConnection()
      .flatMap {
        LoanPattern.futureUsing(_)(db => f(ScalikeJdbcIOContext(db.readOnlySession())))
      }

  override def run[A](f: IOContext => Future[A])(implicit ec: ExecutionContext): Future[A] =
    Future
      .apply(
        DB futureLocalTx { session => f(ScalikeJdbcIOContext(session)) }
      )(getConnectionEc).flatten

  private def getConnection(): Future[DB] =
    Future.apply(DB(ConnectionPool.borrow()))(getConnectionEc)

  lazy val getConnectionEc = actorSystem.dispatchers.lookup("context.db-connection-acquisition")
}
