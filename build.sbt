import Dependencies._

ThisBuild / scalaVersion := "2.13.3"
ThisBuild / version := "1.0.0"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

lazy val domain  = (project in file("domain"))
  .settings(commonSettings: _*)

lazy val playApp = (project in file("playApp"))
  .settings(
    name := "playApp",
    libraryDependencies ++= Seq(
      guice,
      scalikeJdbcCore,
      scalikeJdbcConfig,
      scalikeJdbcPlayInitializer,
      postgresql,
      scalaTestCore         % Test,
      scalamock             % Test,
      mockitoScala          % Test,
      mockitoScalaScalaTest % Test
    ),
    play.sbt.PlayInternalKeys.playCompileEverything ~= (_.map(
      _.copy(compilations = sbt.internal.inc.Compilations.of(Seq.empty))))
  )
  .enablePlugins(PlayScala)
  .disablePlugins(PlayLayoutPlugin)
  .dependsOn(domain)

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
    name := "kleisli"
  )

lazy val freer = (project in file("freer"))
  .settings(
    name := "freer"
  )

lazy val zio = (project in file("zio"))
  .settings(
    name := "zio"
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
  }
)
