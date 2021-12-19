package eventproducer

import cats.effect.std.Random
import cats.effect.{Clock, IO}
import cats.syntax.all._
import eventproducer.model.{Data, Event, EventType, Timestamp}
import fs2._
import fs2.timeseries.TimeStamped

import scala.concurrent.duration.DurationInt

class InfiniteRandomEventProducer extends EventProducer {

  override def getEvent: Stream[IO, TimeStamped[Event]] = {
    (for {
      events <- Stream.eval(randomEvents())
      event <- Stream(events: _*).metered[IO](50.millis)
      timestampedEvent = TimeStamped(event.timestamp.toFiniteDuration, event)
    } yield timestampedEvent).repeat
  }

  private def randomEvents(): IO[List[Event]] = {
    for {
      random <- Random.scalaUtilRandom[IO]
      numberOfEvents <- random.nextIntBounded(20)
      events <- (0 to numberOfEvents).map(_ => randomEvent(random)).toList.sequence
    } yield events
  }

  private def randomEvent(random: Random[IO]) = {
    for {
      now <- Clock[IO].realTime
      eventType <- random.nextIntBounded(10).map(eventType => EventType(eventType.toString))
      data <- randomData(random)
      dateInPast <- random.nextIntBounded(2000).map(millis => now.toMillis - millis).map(Timestamp(_))
    } yield Event(eventType, data, dateInPast)
  }

  private def randomData(random: Random[IO]) = {
    for {
      wordCount <- random.nextIntBounded(10)
      words <- (0 to wordCount).map(_ => random.nextString(10)).toList.sequence
    } yield Data(words.mkString(" "))
  }
}