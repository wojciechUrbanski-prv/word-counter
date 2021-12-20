package com.urbanski.rawdataproducer

import cats.effect.IO
import com.urbanski.rawdataproducer.model.RawData
import fs2._

trait RawDataProducer {
  def getRawData: Stream[IO, RawData]
}
