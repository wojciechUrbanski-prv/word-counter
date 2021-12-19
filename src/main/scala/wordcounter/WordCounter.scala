package wordcounter

import cats.effect.IO
import io.circe.parser._
import eventproducer.EventProducer
import eventproducer.config.EventHandlingConfig
import eventproducer.model.{Event, EventType}
import fs2._
import fs2.timeseries.TimeStamped
import wordcounter.model.WordCount

class WordCounter(eventProducer: EventProducer, config: EventHandlingConfig) {
  def countWords(): Stream[IO, Map[EventType, WordCount]] = {
    eventProducer.getEvent
      .map(rawData => decode[Event](rawData.value))
      .collect { case Right(event) =>
        TimeStamped(event.timestamp.toFiniteDuration, event)
      }
      .through(TimeStamped.reorderLocally(config.reorderingWindow))
      .through(accumulateDataOverTime)
      .collect {
        case TimeStamped(_, Left(values)) if values.nonEmpty => values
      }
      .map(WordCounter.groupAndCountWords)
  }

  private def accumulateDataOverTime(input: Stream[IO, TimeStamped[Event]]) = {
    TimeStamped
      .withRate[Event, List[Event]](config.accumulationWindow)(List(_))
      .toPipe(input)
  }
}

object WordCounter {
  def groupAndCountWords(events: List[Event]): Map[EventType, WordCount] = {
    events.groupBy(_.eventType).view.mapValues(countAllWordsInEvents).toMap
  }

  private def countAllWordsInEvents(events: List[Event]): WordCount = {
    events.foldLeft(WordCount.zero)((currentWordCount, event) => currentWordCount.add(splitDataBySpace(event).count(_.nonEmpty)))
  }

  private def splitDataBySpace(event: Event): Array[String] = {
    event.data.value.split(" ")
  }
}
