package wordcounter

import cats.effect.unsafe.implicits.global
import eventproducer.ListEventProducer
import eventproducer.config.EventHandlingConfig
import eventproducer.model.{Data, Event, EventType, Timestamp}
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import wordcounter.model.WordCount

import scala.concurrent.duration.DurationInt

class WordCounterSpec extends AnyWordSpec {

  "Counting words produced from event stream" should {
    "reorganize events by their timestamp according to config values, group them by event types and count words for each window" in {
      val events = List(
        Event(EventType("eventType1"), Data("data"), Timestamp(1002L)),
        Event(EventType("eventType2"), Data("data"), Timestamp(2003L)),
        Event(EventType("eventType3"), Data("data"), Timestamp(4002L)),
        Event(EventType("eventType4"), Data("data"), Timestamp(3004L)),
        Event(EventType("eventType1"), Data("data"), Timestamp(1001L)),
        Event(EventType("eventType6"), Data("data"), Timestamp(1301L)),
        Event(EventType("eventType1"), Data("data"), Timestamp(11000L)),
      )
      val eventProducer = new ListEventProducer(events)
      val config = EventHandlingConfig(1000.millis, 2000.millis)
      val wordCounter = new WordCounter(eventProducer, config)

      val aggregatedAndCountedEvents = wordCounter.countWords().compile.toList.unsafeRunSync()
      aggregatedAndCountedEvents shouldBe List(
        Map(
          EventType("eventType1") -> WordCount(2),
          EventType("eventType6") -> WordCount(1),
        ),
        Map(EventType("eventType2") -> WordCount(1)),
        Map(EventType("eventType4") -> WordCount(1)),
        Map(EventType("eventType3") -> WordCount(1)),
        Map(EventType("eventType1") -> WordCount(1)),
      )
    }

    "group all events into one map if their timestamps difference is lower than the configured window" in {
      val events = List(
        Event(EventType("eventType1"), Data("data"), Timestamp(1002L)),
        Event(EventType("eventType2"), Data("data"), Timestamp(2003L)),
        Event(EventType("eventType3"), Data("data"), Timestamp(4002L)),
        Event(EventType("eventType4"), Data("data"), Timestamp(3004L)),
        Event(EventType("eventType1"), Data("data"), Timestamp(1001L)),
        Event(EventType("eventType6"), Data("data"), Timestamp(1301L)),
        Event(EventType("eventType1"), Data("data"), Timestamp(11000L)),
      )
      val eventProducer = new ListEventProducer(events)
      val config = EventHandlingConfig(15000.millis, 15000.millis)
      val wordCounter = new WordCounter(eventProducer, config)

      val aggregatedAndCountedEvents = wordCounter.countWords().compile.toList.unsafeRunSync()
      aggregatedAndCountedEvents shouldBe List(
        Map(
          EventType("eventType1") -> WordCount(3),
          EventType("eventType6") -> WordCount(1),
          EventType("eventType2") -> WordCount(1),
          EventType("eventType4") -> WordCount(1),
          EventType("eventType3") -> WordCount(1),
        ),
      )
    }

    "produce empty list if there are no events" in {
      val events = List.empty[Event]
      val eventProducer = new ListEventProducer(events)
      val config = EventHandlingConfig(1000.millis, 2000.millis)
      val wordCounter = new WordCounter(eventProducer, config)

      val aggregatedAndCountedEvents = wordCounter.countWords().compile.toList.unsafeRunSync()
      aggregatedAndCountedEvents shouldBe List()
    }
  }

  "Grouping and counting words from list of events" should {
    "Count correct number of words grouped by event type" in {
      val events = List(
        Event(EventType("eventType1"), Data("some random data"), Timestamp(123456)),
        Event(EventType("eventType1"), Data("random data"), Timestamp(123456)),
        Event(EventType("eventType2"), Data("data2"), Timestamp(123456)),
        Event(EventType("eventType3"), Data("eventType data"), Timestamp(123456)),
      )

      val countedWords = WordCounter.groupAndCountWords(events)
      countedWords shouldBe Map(
        EventType("eventType1") -> WordCount(5),
        EventType("eventType2") -> WordCount(1),
        EventType("eventType3") -> WordCount(2),
      )
    }

    "should not count empty spaces to number of words" in {
      val events = List(Event(EventType("eventType1"), Data(" data  random    withspaces"), Timestamp(123456)))
      val countedWords = WordCounter.groupAndCountWords(events)
      countedWords shouldBe Map(EventType("eventType1") -> WordCount(3))
    }

    "should count 0 words if empty string is provided as a data" in {
      val events = List(Event(EventType("eventType1"), Data(""), Timestamp(123456)))
      val countedWords = WordCounter.groupAndCountWords(events)
      countedWords shouldBe Map(EventType("eventType1") -> WordCount.zero)
    }
  }

}
