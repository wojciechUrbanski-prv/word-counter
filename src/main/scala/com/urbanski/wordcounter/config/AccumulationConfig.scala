package com.urbanski.wordcounter.config

import scala.concurrent.duration.{DurationInt, FiniteDuration}

case class AccumulationConfig(
    accumulationWindow: FiniteDuration,
    reorderingWindow: FiniteDuration
)

object AccumulationConfig {
  val default: AccumulationConfig = AccumulationConfig(accumulationWindow = 2000.millis, reorderingWindow = 2000.millis)
}
