package processor

import eventproducer.model.{Event, EventType}
import processor.model.WordCount

object WordCounter {

  def countWords(events: List[Event]): Map[EventType, WordCount] = {
    events.groupBy(_.eventType).view.mapValues(countAllWordsInEvents).toMap
  }

  private def countAllWordsInEvents(events: List[Event]): WordCount = {
    events.foldLeft(WordCount.zero)((currentWordCount, event) => currentWordCount.add(splitDataBySpace(event).count(_.nonEmpty)))
  }

  private def splitDataBySpace(event: Event): Array[String] = {
    event.data.value.split(" ")
  }

}
