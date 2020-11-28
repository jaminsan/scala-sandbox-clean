package com.example

import javax.inject.{ Inject, Singleton }
import play.api.Environment
import play.api.http.JsonHttpErrorHandler

@Singleton
class AppErrorHandler @Inject() (env: Environment) extends JsonHttpErrorHandler(env, None)
