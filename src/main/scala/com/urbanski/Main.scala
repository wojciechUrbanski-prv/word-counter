package com.urbanski

import cats.effect.{ExitCode, IO, IOApp}
import com.urbanski.rawdataproducer.InfiniteRandomRawDataProducer
import com.urbanski.http.GetWordsService
import com.urbanski.store.RefWordStore
import org.http4s.blaze.server.BlazeServerBuilder

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    for {
      store <- RefWordStore.make
      rawDataProducer = new InfiniteRandomRawDataProducer
      coreService     = CoreService.makeLive(store, rawDataProducer)
      coreServiceFiber <- coreService.countAndStoreWords().compile.drain.start
      getWordsService = new GetWordsService(store)
      httpServerFiber <- runHttpServer(getWordsService).start
      _               <- coreServiceFiber.join
      _               <- httpServerFiber.join
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
