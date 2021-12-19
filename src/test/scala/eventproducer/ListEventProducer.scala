package eventproducer

import cats.effect.IO
import eventproducer.model.Event
import fs2._
import fs2.timeseries.TimeStamped

class ListEventProducer(events: List[Event]) extends EventProducer {
  override def getEvent: Stream[IO, TimeStamped[Event]] = {
    for {
      event <- Stream(events: _*)
      timestampedEvent = TimeStamped(event.timestamp.toFiniteDuration, event)
    } yield timestampedEvent
  }
}
