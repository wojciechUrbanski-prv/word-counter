ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.7"

lazy val root = (project in file("."))
  .settings(
    name := "word-counter",
    libraryDependencies ++=
      Dependencies.fs2 ++
        Dependencies.scalatest ++
        Dependencies.logging ++
        Dependencies.http4s ++
        Dependencies.circe
  )
