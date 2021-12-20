package com.urbanski.wordcounter.model

import io.circe.generic.extras.semiauto.{deriveUnwrappedDecoder, deriveUnwrappedEncoder}
import io.circe.{Decoder, Encoder, KeyEncoder}

case class EventType(value: String) extends AnyVal

object EventType {
  implicit val eventTypeEncoder: Encoder[EventType]       = deriveUnwrappedEncoder
  implicit val eventTypeDecoder: Decoder[EventType]       = deriveUnwrappedDecoder
  implicit val eventTypeKeyEncoder: KeyEncoder[EventType] = KeyEncoder[EventType](eventType => eventType.value)
}
