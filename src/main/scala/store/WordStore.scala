package store

import cats.effect.IO
import eventproducer.model.EventType
import wordcounter.model.WordCount

trait WordStore {
  def store(newWordCounts: Map[EventType, WordCount]): IO[Unit]
  def get(): IO[Map[EventType, WordCount]]
}
