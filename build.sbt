import Dependencies._

ThisBuild / scalaVersion := "2.13.3"
ThisBuild / version := "1.0.0"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

lazy val domain           = (project in file("domain"))
  .settings(commonSettings: _*)

lazy val playAppExtension = (project in file("playApp/extension"))
  .settings(
    name := "playAppExtension"
  )
  .settings(commonSettings: _*)

lazy val playAppDriver    = (project in file("playApp/driver"))
  .settings(
    name := "playAppDriver",
    libraryDependencies ++= Seq(
      guice,
      scalikeJdbcCore,
      scalaTestCore % Test,
      scalamock     % Test
    )
  )
  .dependsOn(playAppExtension)
  .settings(commonSettings: _*)

lazy val playAppPort      = (project in file("playApp/port"))
  .settings(
    name := "playAppPort"
  )
  .dependsOn(domain, playAppExtension)

lazy val playAppUseCase = (project in file("playApp/usecase"))
  .settings(
    name := "playAppUseCase",
    libraryDependencies ++= Seq(
      guice,
      scalaTestCore % Test,
      scalamock     % Test,
      mockitoScala  % Test
    )
  )
  .dependsOn(domain, playAppExtension % "test->test;compile->compile", playAppPort)
  .settings(commonSettings: _*)

lazy val playAppGateway = (project in file("playApp/gateway"))
  .settings(
    name := "playAppGateway",
    libraryDependencies ++= Seq(
      guice,
      scalaTestCore % Test,
      scalamock     % Test
    )
  )
  .dependsOn(domain, playAppExtension, playAppPort, playAppDriver)
  .settings(commonSettings: _*)

def nonRootPlayProjects = Seq(
  domain,
  playAppExtension,
  playAppPort,
  playAppUseCase,
  playAppDriver,
  playAppGateway
)

lazy val playAppRest        = (project in file("playApp/rest"))
  .settings(
    name := "playAppRest",
    libraryDependencies ++= Seq(
      guice,
      scalikeJdbcCore,
      scalikeJdbcConfig,
      jdbc,
      postgresql
    ),
    assembly / test := {
      // FIXME: DRY
      (domain / assemblyPackageDependency / test).value
      (playAppExtension / assemblyPackageDependency / test).value
      (playAppPort / assemblyPackageDependency / test).value
      (playAppUseCase / assemblyPackageDependency / test).value
      (playAppDriver / assemblyPackageDependency / test).value
      (playAppGateway / assemblyPackageDependency / test).value
    }
  )
  .enablePlugins(PlayScala)
  .disablePlugins(PlayLayoutPlugin)
  .dependsOn(domain, playAppExtension, playAppPort, playAppUseCase, playAppDriver, playAppGateway)
  .settings(commonSettings: _*)

lazy val abstractTypeMember = (project in file("abstractTypeMember"))
  .settings(
    name := "abstractTypeMember"
  )

lazy val eitherT = (project in file("eitherT"))
  .settings(
    name := "eitherT"
  )

lazy val kleisli = (project in file("kleisli"))
  .settings(
    name := "kleisli",
    libraryDependencies ++= Seq(
      guice,
      catsCore,
      scalaTestCore,
      scalikeJdbcCore,
      scalamock
    )
  )
  .enablePlugins(PlayScala)
  .disablePlugins(PlayLayoutPlugin)
  .dependsOn(domain)

lazy val freer = (project in file("freer"))
  .settings(
    name := "freer"
  )

lazy val zio = (project in file("zio"))
  .settings(
    name := "zio"
  )

lazy val trial = (project in file("trial"))
  .settings(
    name := "trial",
    libraryDependencies ++= Seq(
      scalaTestCore % Test,
      mockitoScala  % Test
    )
  )

lazy val commonSettings = Seq(
//  Test / fork := true,
//  Test / parallelExecution := true,
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  ),
  addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.11.0" cross CrossVersion.full),
  addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
  ThisBuild / scalafmtOnCompile := true,
  ThisBuild / scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-language:higherKinds",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-feature",
    "-Xfatal-warnings"
  ),
  assembly / assemblyJarName := {
    val projectName = name.value
    s"$projectName.jar"
  },
  assemblyMergeStrategy in assembly := {
    case "application.conf" => MergeStrategy.concat
    case "play/reference-overrides.conf" => MergeStrategy.concat
    case "module-info.class" => MergeStrategy.discard
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  },
  excludeDependencies ++= Seq(
    ExclusionRule("commons-logging", "commons-logging")
  )
)
