package com.urbanski.wordcounter.model

import io.circe.generic.extras.semiauto.{deriveUnwrappedDecoder, deriveUnwrappedEncoder}
import io.circe.{Decoder, Encoder}

case class Data(value: String) extends AnyVal

object Data {
  implicit val dataEncoder: Encoder[Data] = deriveUnwrappedEncoder
  implicit val dataDecoder: Decoder[Data] = deriveUnwrappedDecoder
}
