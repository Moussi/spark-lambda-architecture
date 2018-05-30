package functions

/**
  * Created by moussi on 29/05/18.
  */
object Loops extends App {

 val words = List("moussi", "aymen")
 val numbers = List(1, 2, 3, 4, 5)

  /**
    * yield to make functional loop expression that return a value
    */
  val result = for {word <- words
                    number <- numbers} yield word + " => " + number
  println(result)

  val square = for (number <- numbers if number % 2 == 0) yield number * number
  println(square)
}
