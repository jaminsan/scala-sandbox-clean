package com.example.driver

import com.example.`extension`.IOContext
import scalikejdbc.DBSession

case class ScalikeJdbcIOContext(session: DBSession) extends IOContext

object ScalikeJdbcIOContext {

  implicit def toDBSession(implicit ctx: IOContext): DBSession =
    ctx match {
      case ScalikeJdbcIOContext(s) => s
      case _ => throw new IllegalArgumentException(s"DBSession required but received ctx:$ctx")
    }
}
