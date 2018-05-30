package functions

/**
  * Created by moussi on 29/05/18.
  */
object functions extends App{
  case class Fruit(name:String)
  val apple = Fruit("apple")
  val orange = Fruit("orange")
  val mangue = Fruit("mangue")
  val kiwi = Fruit("kiwi")
  val fruits = apple::orange::apple::mangue::apple::orange::kiwi::Nil

  val apples = fruits.filter(_.name == "apple")
  println(apples)
}
