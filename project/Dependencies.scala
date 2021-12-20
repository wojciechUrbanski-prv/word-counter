import sbt._

object Dependencies {

  val circe = Seq(
    "io.circe" %% "circe-core"           % Versions.circe,
    "io.circe" %% "circe-generic-extras" % Versions.circe,
    "io.circe" %% "circe-parser"         % Versions.circe
  )

  val fs2 = Seq(
    "co.fs2" %% "fs2-core" % Versions.fs2,
    "co.fs2" %% "fs2-io"   % Versions.fs2
  )

  val http4s = Seq(
    "org.http4s" %% "http4s-blaze-server" % Versions.http4s,
    "org.http4s" %% "http4s-circe"        % Versions.http4s,
    "org.http4s" %% "http4s-dsl"          % Versions.http4s
  )

  val logging = Seq(
    "ch.qos.logback" % "logback-classic" % Versions.logback,
    "org.typelevel" %% "log4cats-slf4j"  % Versions.log4cats
  )

  val scalatest = Seq(
    "org.scalatest"  %% "scalatest"  % Versions.scalatest  % "test",
    "org.scalacheck" %% "scalacheck" % Versions.scalacheck % "test"
  )
}

object Versions {
  val circe      = "0.14.1"
  val fs2        = "3.2.3"
  val http4s     = "1.0.0-M30"
  val logback    = "1.2.9"
  val log4cats   = "2.1.1"
  val scalatest  = "3.2.9"
  val scalacheck = "1.15.4"
}
