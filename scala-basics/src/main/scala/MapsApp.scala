/**
  * Created by moussi on 12/05/18.
  */
object MapsApp {
  def main(args: Array[String]): Unit = {
    var mappy:Map[Int, String] = Map()
    mappy += (1 -> "moussi")
    mappy += (2 -> "Aymen")

    println(mappy.values)
    println(mappy.keys)

    var mappy2 = Map(1 -> "Aymen", 2 -> "Moussi")

    val option = mappy2.get(1)
    println(option.get)
  }
}
