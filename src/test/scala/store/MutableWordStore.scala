package store
import cats.effect.IO
import eventproducer.model.EventType
import wordcounter.model.WordCount

import scala.collection.mutable

class MutableWordStore extends WordStore {
  private val wordStore = mutable.Map.empty[EventType, WordCount]

  override def store(newWordCounts: Map[EventType, WordCount]): IO[Unit] = {
    wordStore.clear()
    IO(wordStore.addAll(newWordCounts))
  }

  override def get(): IO[Map[EventType, WordCount]] = IO(wordStore.toMap)
}
