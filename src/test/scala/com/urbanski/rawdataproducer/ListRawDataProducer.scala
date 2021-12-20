package com.urbanski.rawdataproducer

import cats.effect.IO
import com.urbanski.rawdataproducer.model.RawData
import fs2._

class ListRawDataProducer(data: List[RawData]) extends RawDataProducer {
  override def getRawData: Stream[IO, RawData] = {
    Stream(data: _*)
  }
}
