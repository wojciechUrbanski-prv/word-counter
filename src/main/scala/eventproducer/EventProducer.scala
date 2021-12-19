package eventproducer

import cats.effect.IO
import eventproducer.model.RawData
import fs2._

trait EventProducer {
  def getEvent: Stream[IO, RawData]
}
