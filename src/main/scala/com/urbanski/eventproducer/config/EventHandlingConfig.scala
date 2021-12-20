package com.urbanski.eventproducer.config

import scala.concurrent.duration.{DurationInt, FiniteDuration}

case class EventHandlingConfig(
    accumulationWindow: FiniteDuration,
    reorderingWindow: FiniteDuration
)

object EventHandlingConfig {
  val default: EventHandlingConfig = EventHandlingConfig(2000.millis, 2000.millis)
}
