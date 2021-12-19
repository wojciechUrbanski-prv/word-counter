package eventproducer

import cats.effect.std.Random
import cats.effect.{Clock, IO}
import cats.syntax.all._
import eventproducer.model._
import io.circe.syntax._
import fs2._

import scala.concurrent.duration.DurationInt

class InfiniteRandomEventProducer extends EventProducer {

  override def getEvent: Stream[IO, RawData] = {
    Stream.eval(randomEventOrGarbageData()).repeat.metered(50.millis)
  }

  private def randomEventOrGarbageData(): IO[RawData] = {
    for {
      random            <- Random.scalaUtilRandom[IO]
      shouldBeEventData <- random.nextBoolean
      data <-
        if (shouldBeEventData) {
          randomEvent(random).map(event => event.asJson.toString)
        } else {
          random.nextString(10)
        }
    } yield RawData(data)
  }

  private def randomEvent(random: Random[IO]) = {
    for {
      now        <- Clock[IO].realTime
      eventType  <- random.nextIntBounded(10).map(eventType => EventType(eventType.toString))
      data       <- randomData(random)
      dateInPast <- random.nextIntBounded(2000).map(millis => now.toMillis - millis).map(Timestamp(_))
    } yield Event(eventType, data, dateInPast)
  }

  private def randomData(random: Random[IO]) = {
    for {
      wordCount <- random.nextIntBounded(10)
      words     <- (0 to wordCount).map(_ => random.nextString(10)).toList.sequence
    } yield Data(words.mkString(" "))
  }
}
