import cats.effect._
import http.GetWordsService
import org.http4s.blaze.server._
import store.RefWordStore

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    for {
      store <- RefWordStore.make
      coreService = CoreService.makeLive(store)
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
