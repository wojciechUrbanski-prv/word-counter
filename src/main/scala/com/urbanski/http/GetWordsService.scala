package com.urbanski.http

import cats.data.Kleisli
import cats.effect.IO
import com.urbanski.store.WordStore
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.{HttpRoutes, Request, Response}

class GetWordsService(wordsStore: WordStore) {

  val getWords: Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes
    .of[IO] { case GET -> Root / "words" =>
      wordsStore.get().flatMap(words => Ok(words.asJson))
    }
    .orNotFound

}
