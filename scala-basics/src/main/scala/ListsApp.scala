/**
  * Created by moussi on 12/05/18.
  */
object ListsApp {
  def main(args: Array[String]): Unit = {
    val fruits = List("orange", "apple")
    val fruitsCons = "orange"::"apple"::Nil
    val multiFruits = List(List("orange","apple"), List("fraise"))
    val multiFruitsCons = ("orange"::"apple"::Nil)::("fruit"::"fraise"::"strawberry"::Nil)::Nil

    val flattenFruit = multiFruitsCons.filter(list => list.size > 2)

    val stringsToOnceToList = flattenFruit.flatten

    stringsToOnceToList.foreach(println)
  }
}
