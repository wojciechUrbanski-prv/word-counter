package com.urbanski.store

import cats.effect.IO
import com.urbanski.eventproducer.model.EventType
import com.urbanski.wordcounter.model.WordCount

trait WordStore {
  def store(newWordCounts: Map[EventType, WordCount]): IO[Unit]
  def get(): IO[Map[EventType, WordCount]]
}
