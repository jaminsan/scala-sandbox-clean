package com.example.gateway

import com.example.port.IOContext
import scalikejdbc.DBSession

case class ScalikeJdbcContext(session: DBSession) extends IOContext

object ScalikeJdbcContext {

  implicit def ioContextAsDBSession(implicit ctx: IOContext): DBSession =
    ctx match {
      case ScalikeJdbcContext(s) => s
      case _ => throw new IllegalArgumentException(s"DBSession required but received ctx:$ctx")
    }
}
