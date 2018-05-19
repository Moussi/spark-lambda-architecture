/**
  * Created by moussi on 12/05/18.
  */
object TuplesApps {
  def main(args: Array[String]): Unit = {
    val tuple = new Tuple2("1","2")
    val tuple2 = ("1", "2")
    val tuple3 = tuple2.swap

    tuple2.productIterator.foreach(println)
    tuple3.productIterator.foreach(println)
  }
}
