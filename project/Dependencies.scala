import sbt._

object Dependencies {
  lazy val logbackClassic = "ch.qos.logback" % "logback-classic" % "0.13.0"

  lazy val scalaTestCore = "org.scalatest" %% "scalatest"     % "3.2.2"
  lazy val mUnit         = "org.scalameta" %% "munit"         % "0.7.17"
  lazy val specs2Core    = "org.specs2"    %% "specs2-core"   % "4.10.0"
  lazy val mockitoScala  = "org.mockito"   %% "mockito-scala" % "1.16.3"
  lazy val scalamock     = "org.scalamock" %% "scalamock"     % "4.4.0"

  lazy val testcontainersScalaTest  = "com.dimafeng" %% "testcontainers-scala-scalatest"  % "0.38.6"
  lazy val testcontainersScalaMunit = "com.dimafeng" %% "testcontainers-scala-scalatest"  % "0.38.6"
  lazy val testcontainersMysql      = "com.dimafeng" %% "testcontainers-scala-mysql"      % "0.38.6"
  lazy val testcontainersPostgresql = "com.dimafeng" %% "testcontainers-scala-postgresql" % "0.38.6"

  lazy val airframeLog = "org.wvlet.airframe" %% "airframe-log"          % "20.10.3"
  lazy val enumeratum  = "com.beachape"       %% "enumeratum"            % "1.6"
  lazy val chimney     = "io.scalaland"       %% "chimney"               % "0.6.1"
  lazy val sulkyUlid   = "de.huxhorn.sulky"    % "de.huxhorn.sulky.ulid" % "8.2.0"
  lazy val osLib       = "com.lihaoyi"        %% "os-lib"                % "0.7.1"

  lazy val catsCore   = "org.typelevel" %% "cats-core"   % "2.2.0"
  lazy val catsEffect = "org.typelevel" %% "cats-effect" % "2.2.0"

  lazy val zio = "dev.zio" %% "zio" % "1.0.3"

  lazy val sttpClient = "com.softwaremill.sttp.client3" %% "core"                    % "3.0.0-RC9"
  lazy val playWsCore = "com.typesafe.play"             %% "play-ahc-ws-standalone"  % "2.1.2"
  lazy val playWsJson = "com.typesafe.play"             %% "play-ws-standalone-json" % "2.1.2"

  lazy val http4sBlazeServer = "org.http4s" %% "http4s-blaze-server" % "1.0.0-M5"
  lazy val http4sBlazeClient = "org.http4s" %% "http4s-blaze-client" % "1.0.0-M5"
  lazy val htt4sCirce        = "org.http4s" %% "http4s-circe"        % "1.0.0-M5"
  lazy val http4sDsl         = "org.http4s" %% "http4s-dsl"          % "1.0.0-M5"
  lazy val circeGenerics     = "io.circe"   %% "circe-generic"       % "0.13.0"

  lazy val scalikeJdbcCore   = "org.scalikejdbc" %% "scalikejdbc"        % "3.5.0"
  lazy val scalikeJdbcConfig = "org.scalikejdbc" %% "scalikejdbc-config" % "3.5.0"
  lazy val doobieCore        = "org.tpolecat"    %% "doobie-core"        % "0.9.0"
  lazy val doobieHikari      = "org.tpolecat"    %% "doobie-hikari"      % "0.9.0"
  lazy val doobiePostgresql  = "org.tpolecat"    %% "doobie-postgres"    % "0.9.0"
  lazy val skunk             = "org.tpolecat"    %% "skunk-core"         % "0.0.20"

  lazy val mysql      = "mysql"          % "mysql-connector-java" % "8.0.18"
  lazy val postgresql = "org.postgresql" % "postgresql"           % "42.2.18"
}
