package com.urbanski

import cats.effect.IO
import com.urbanski.eventproducer.RawDataProducer
import com.urbanski.eventproducer.config.EventHandlingConfig
import com.urbanski.store.WordStore
import com.urbanski.wordcounter.WordCounter

class CoreService private (wordStore: WordStore, wordCounter: WordCounter) {

  def countAndStoreWords(): fs2.Stream[IO, Unit] = {
    wordCounter.countWords().evalMap(wordStore.store)
  }

}

object CoreService {
  def makeLive(store: WordStore, rawDataProducer: RawDataProducer, config: EventHandlingConfig = EventHandlingConfig.default): CoreService = {
    val wordCounter = new WordCounter(rawDataProducer, config)
    new CoreService(store, wordCounter)
  }
}