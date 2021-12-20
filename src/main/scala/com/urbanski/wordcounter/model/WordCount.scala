package com.urbanski.wordcounter.model

import io.circe.generic.extras.semiauto.{deriveUnwrappedDecoder, deriveUnwrappedEncoder}
import io.circe.{Decoder, Encoder}

case class WordCount(value: Int) extends AnyVal {
  def add(count: Int): WordCount = WordCount(value + count)
}

object WordCount {
  val zero: WordCount = WordCount(0)

  implicit val wordCountEncoder: Encoder[WordCount] = deriveUnwrappedEncoder
  implicit val wordCountDecoder: Decoder[WordCount] = deriveUnwrappedDecoder
}
