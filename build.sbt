ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.7"

lazy val root = (project in file("."))
  .settings(
    name := "word-counter",
    libraryDependencies ++= Seq(
      "co.fs2" %% "fs2-core" % "3.2.3",
      "co.fs2" %% "fs2-io" % "3.2.3",
      "org.scalatest" %% "scalatest" % "3.2.9" % "test",
      "org.scalacheck" %% "scalacheck" % "1.15.4" % "test",
      "ch.qos.logback" % "logback-classic" % "1.2.9",
      "org.typelevel" %% "log4cats-slf4j" % "2.1.1",
      "org.http4s" %% "http4s-blaze-server" % "1.0.0-M30",
      "org.http4s" %% "http4s-circe" % "1.0.0-M30",
      "org.http4s" %% "http4s-dsl" % "1.0.0-M30",
    )
  )
