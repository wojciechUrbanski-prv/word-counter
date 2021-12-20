package com.urbanski.store

import cats.effect.IO
import com.urbanski.wordcounter.model.{EventType, WordCount}

trait WordStore {
  def store(newWordCounts: Map[EventType, WordCount]): IO[Unit]
  def get(): IO[Map[EventType, WordCount]]
}
