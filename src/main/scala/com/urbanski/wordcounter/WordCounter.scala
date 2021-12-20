package com.urbanski.wordcounter

import cats.effect.IO
import com.urbanski.eventproducer.RawDataProducer
import com.urbanski.eventproducer.config.EventHandlingConfig
import com.urbanski.eventproducer.model.{Event, EventType}
import io.circe.parser._
import fs2._
import fs2.timeseries.TimeStamped
import org.typelevel.log4cats.slf4j.Slf4jLogger
import com.urbanski.wordcounter.model.WordCount

class WordCounter(rawDataProducer: RawDataProducer, config: EventHandlingConfig) {
  def countWords(): Stream[IO, Map[EventType, WordCount]] = {
    rawDataProducer.getRawData
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
      .evalTap(logCollectedDataInfo)
  }

  private def accumulateDataOverTime(input: Stream[IO, TimeStamped[Event]]) = {
    TimeStamped
      .withRate[Event, List[Event]](config.accumulationWindow)(List(_))
      .toPipe(input)
  }

  private def logCollectedDataInfo(collectedData: Map[EventType, WordCount]) = {
    val logData = collectedData
      .map { case (eventType, wordCount) =>
        s"$eventType: $wordCount"
      }
      .mkString("\n", "\n", "")
    Slf4jLogger.create[IO].flatMap(logger => logger.info(s"Collected: $logData"))
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
