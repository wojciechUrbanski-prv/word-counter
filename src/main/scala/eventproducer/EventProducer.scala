package eventproducer

import cats.effect.IO
import fs2._
import eventproducer.model.Event
import fs2.timeseries.TimeStamped

trait EventProducer {
  def getEvent : Stream[IO, TimeStamped[Event]]
}
