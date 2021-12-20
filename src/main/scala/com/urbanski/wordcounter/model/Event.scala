package com.urbanski.wordcounter.model

import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._
import io.circe.{Decoder, Encoder}

case class Event(eventType: EventType, data: Data, timestamp: Timestamp)
object Event {
  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val eventEncoder: Encoder[Event] = deriveConfiguredEncoder
  implicit val eventDecoder: Decoder[Event] = deriveConfiguredDecoder
}
