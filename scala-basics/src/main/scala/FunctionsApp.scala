import java.util.Date

/**
  * Created by moussi on 12/05/18.
  */
object FunctionsApp {

  def printStrings(args:String*): Unit ={
    for(arg <- args){
      println("arg : "+arg)
    }
  }

  def add(a: Int = 5, b: Int = 7): Int = {
    a + b
  }

  def layout[A](x:A) = s"[$x]"

  def apply(f:Int => String, v:Int) = f(v)

  def log(date: Date, message: String)  = {
    println(date + "----" + message)
  }

  def strcat(s1: String)(s2: String) = s1 + s2

  def main(args: Array[String]): Unit = {
    printStrings("Moussi", "Aymen")
    /**
      * call function with defaults
      */
    println(add())
    /**
      * Highre Order and Generics Functions
      */
    println(apply(layout, 10))
    /**
      * partially applied function
      */
    val date = new Date()
    var partiallyLog = log(date, _:String);
    partiallyLog("message1")
    partiallyLog("message2")
    partiallyLog("message3")
    /**
      * Carried Functions
      */
    println(strcat("Moussi")("Aymen"))
  }

}
