package eventproducer.config

import scala.concurrent.duration.FiniteDuration

case class EventHandlingConfig(
    accumulationWindow: FiniteDuration,
    reorderingWindow: FiniteDuration
)
