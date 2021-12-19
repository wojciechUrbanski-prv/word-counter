package processor

import eventproducer.model.{Data, Event, EventType, Timestamp}
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import processor.model.WordCount

class WordCounterSpec extends AnyWordSpec {

  "WordCounter" should {
    "Count correct number of words grouped by event type" in {
      val events = List(
        Event(EventType("eventType1"), Data("some random data"), Timestamp(123456)),
        Event(EventType("eventType1"), Data("random data"), Timestamp(123456)),
        Event(EventType("eventType2"), Data("data2"), Timestamp(123456)),
        Event(EventType("eventType3"), Data("eventType data"), Timestamp(123456)),
      )

      val countedWords = WordCounter.countWords(events)
      countedWords shouldBe Map(
        EventType("eventType1") -> WordCount(5),
        EventType("eventType2") -> WordCount(1),
        EventType("eventType3") -> WordCount(2),
      )
    }

    "should not count empty spaces to number of words" in {
      val events = List(Event(EventType("eventType1"), Data(" data  random    withspaces"), Timestamp(123456)))
      val countedWords = WordCounter.countWords(events)
      countedWords shouldBe Map(EventType("eventType1") -> WordCount(3))
    }

    "should count 0 words if empty string is provided as a data" in {
      val events = List(Event(EventType("eventType1"), Data(""), Timestamp(123456)))
      val countedWords = WordCounter.countWords(events)
      countedWords shouldBe Map(EventType("eventType1") -> WordCount.zero)
    }
  }

}
