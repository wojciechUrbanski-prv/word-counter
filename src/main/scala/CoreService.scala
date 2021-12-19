import cats.effect.IO
import eventproducer.InfiniteRandomEventProducer
import eventproducer.config.EventHandlingConfig
import store.WordStore
import wordcounter.WordCounter

import scala.concurrent.duration.DurationInt

class CoreService private (wordStore: WordStore, wordCounter: WordCounter) {

  def countAndStoreWords(): fs2.Stream[IO, Unit] = {
    wordCounter.countWords().evalMap(wordStore.store)
  }

}

object CoreService {
  def makeLive(store: WordStore): CoreService = {
    val eventProducer = new InfiniteRandomEventProducer
    val config        = EventHandlingConfig(2000.millis, 2000.millis)
    val wordCounter   = new WordCounter(eventProducer, config)
    new CoreService(store, wordCounter)
  }
}
