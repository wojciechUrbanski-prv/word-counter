package com.urbanski.eventproducer

import cats.effect.IO
import com.urbanski.eventproducer.model.RawData
import fs2._

trait EventProducer {
  def getEvent: Stream[IO, RawData]
}
