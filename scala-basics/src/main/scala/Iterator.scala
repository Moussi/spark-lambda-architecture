/**
  * Created by moussi on 13/05/18.
  */
sealed trait Iterator[A] {
  def hasNext: Boolean

  def next(): A
}

case class IntIterator(to: Int) extends Iterator[Int] {
  private var current = 0;

  override def hasNext = current < to

  override def next() = {
    if (hasNext) {
      val t = current
      current += 1
      t
    } else 0
  }
}
