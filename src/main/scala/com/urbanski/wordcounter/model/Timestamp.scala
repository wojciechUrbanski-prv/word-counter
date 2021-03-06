package com.urbanski.wordcounter.model

import io.circe.generic.extras.semiauto.{deriveUnwrappedDecoder, deriveUnwrappedEncoder}
import io.circe.{Decoder, Encoder}

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration

case class Timestamp(value: Long) extends AnyVal {
  def toFiniteDuration: FiniteDuration = FiniteDuration(value, TimeUnit.MILLISECONDS)
}

object Timestamp {
  implicit val timestampEncoder: Encoder[Timestamp] = deriveUnwrappedEncoder
  implicit val timestampDecoder: Decoder[Timestamp] = deriveUnwrappedDecoder
}
