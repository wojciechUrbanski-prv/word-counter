package http

import cats.effect.IO
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.io._
import store.WordStore

class GetWordsService(wordsStore: WordStore) {

  val getWords = HttpRoutes
    .of[IO] { case GET -> Root / "words" =>
      wordsStore.get().flatMap(words => Ok(words.asJson))
    }
    .orNotFound

}
