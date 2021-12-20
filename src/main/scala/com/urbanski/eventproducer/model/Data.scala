package com.urbanski.eventproducer.model

import io.circe.{Decoder, Encoder}
import io.circe.generic.extras.semiauto.{deriveUnwrappedDecoder, deriveUnwrappedEncoder}

case class Data(value: String) extends AnyVal

object Data {
  implicit val dataEncoder: Encoder[Data] = deriveUnwrappedEncoder
  implicit val dataDecoder: Decoder[Data] = deriveUnwrappedDecoder
}
