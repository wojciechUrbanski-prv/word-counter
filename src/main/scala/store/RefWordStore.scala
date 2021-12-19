package store

import cats.effect.{IO, Ref}
import eventproducer.model.EventType
import wordcounter.model.WordCount

class RefWordStore private(wordStore: Ref[IO, Map[EventType, WordCount]]) extends WordStore {
  override def store(newWordCounts: Map[EventType, WordCount]): IO[Unit] = wordStore.set(newWordCounts)

  override def get(): IO[Map[EventType, WordCount]] = wordStore.get
}

object RefWordStore {
  def make: IO[RefWordStore] = Ref.of[IO, Map[EventType, WordCount]](Map.empty).map(store => new RefWordStore(store))
}
