package com.example

import akka.actor.ActorSystem
import com.example.gateway.DbAccessExecutionContext
import javax.inject.{ Inject, Singleton }
import play.api.libs.concurrent.CustomExecutionContext

@Singleton
class DbAccessExecutionContextImpl @Inject() (actorSystem: ActorSystem)
    extends CustomExecutionContext(actorSystem, "context.db-access")
    with DbAccessExecutionContext
