
/**
  * Created by moussi on 12/05/18.
  */
object App {
  def main(args: Array[String]): Unit = {
    val point = new Point(2, 6)
    point.move(4, 7)
    val c = 4 >> 10
    println("**********"+c)
    val location = new Location(10, 20, 30)
    location.move(5, 5, 5)
  }
}
