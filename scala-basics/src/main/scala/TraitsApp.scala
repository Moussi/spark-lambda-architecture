/**
  * Created by moussi on 13/05/18.
  */
object TraitsApp {

  def main(args: Array[String]): Unit = {
    val iterator = IntIterator(3)
    iterator.hasNext
    println(iterator.next)
    println(iterator.next)
    println(iterator.next)
    println(iterator.next)
    println(iterator.next)
  }
}
