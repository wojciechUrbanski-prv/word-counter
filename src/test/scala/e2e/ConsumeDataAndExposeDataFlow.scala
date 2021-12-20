package e2e

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.urbanski.CoreService
import com.urbanski.http.GetWordsService
import com.urbanski.rawdataproducer.ListRawDataProducer
import com.urbanski.rawdataproducer.model.RawData
import com.urbanski.store.MutableWordStore
import com.urbanski.wordcounter.model._
import io.circe.parser._
import io.circe.syntax._
import org.http4s._
import org.http4s.implicits._
import org.scalatest.EitherValues
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

class ConsumeDataAndExposeDataFlow extends AnyWordSpec with EitherValues {

  "Application" should {
    "Consume streamed data, expose grouped and counted data via HTTP endpoint" in {
      val events = List(
        Event(EventType("eventType1"), Data("data"), Timestamp(1002L)).asJson.toString,
        """{ "event_type": "eventType1", "data": "data" }""",
        Event(EventType("eventType2"), Data("data"), Timestamp(2003L)).asJson.toString,
        """{ "randomData": "1231"}""",
        Event(EventType("eventType1"), Data("data"), Timestamp(1001L)).asJson.toString
      ).map(RawData)
      val store           = new MutableWordStore
      val rawDataProducer = new ListRawDataProducer(events)
      val coreService     = CoreService.makeLive(store, rawDataProducer)
      val getWordsService = new GetWordsService(store)

      val response = (for {
        _ <- coreService.countAndStoreWords().compile.drain
        response <- getWordsService.getWords.run(
          Request(
            method = Method.GET,
            uri = uri"/words"
          )
        )
      } yield response).unsafeRunSync()

      response.status shouldBe Status.Ok
      val result = convertBodyToMap(response)
      result.value shouldBe Map(
        "eventType1" -> WordCount(2),
        "eventType2" -> WordCount(1)
      )
    }
  }

  private def convertBodyToMap(response: Response[IO]) =
    response.as[String].map(stringBody => parse(stringBody).flatMap(_.as[Map[String, WordCount]])).unsafeRunSync()

}
