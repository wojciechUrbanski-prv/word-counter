package com.urbanski

import cats.effect.{ExitCode, IO, IOApp}
import com.urbanski.eventproducer.InfiniteRandomEventProducer
import com.urbanski.http.GetWordsService
import com.urbanski.store.RefWordStore
import org.http4s.blaze.server.BlazeServerBuilder

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    for {
      store <- RefWordStore.make
      eventProducer = new InfiniteRandomEventProducer
      coreService   = CoreService.makeLive(store, eventProducer)
      _ <- coreService.countAndStoreWords().compile.drain.start
      getWordsService = new GetWordsService(store)
      _ <- runHttpServer(getWordsService)
    } yield ExitCode.Success
  }

  private def runHttpServer(getWordsService: GetWordsService) = {
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(getWordsService.getWords)
      .serve
      .compile
      .drain
  }
}
