package eventproducer

import cats.effect.IO
import eventproducer.model.RawData
import fs2._

class ListEventProducer(data: List[RawData]) extends EventProducer {
  override def getEvent: Stream[IO, RawData] = {
    Stream(data: _*)
  }
}
