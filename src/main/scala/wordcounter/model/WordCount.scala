package wordcounter.model

case class WordCount(value: Int) extends AnyVal {
  def add(count: Int): WordCount = WordCount(value + count)
}

object WordCount {
  val zero: WordCount = WordCount(0)
}