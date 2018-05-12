import StringUtils._

/**
  * Created by moussi on 12/05/18.
  */
object ImplicitClassApp {
  def main(args: Array[String]): Unit = {

    implicit def iAm() : String = "Moussi"

    println("moussi".upper())
    println("moussi".addSymbolAtTheEnd('&'))
    println("moussi".sayHello)
  }
}
