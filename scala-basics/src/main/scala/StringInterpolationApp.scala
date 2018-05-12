/**
  * Created by moussi on 12/05/18.
  */
object StringInterpolationApp {
  def main(args: Array[String]): Unit = {
    val home= "Puteaux"
    val height = 5.257
    println(s"I am from $home")
    println(s"1 + 1 =  ${1+1}")
    println(f"my home is $home%s and has height $height%2.2f")
    println(s"result \n a \n b")
    /**
      * raw trace the escape chars
      */
    println(raw"result \n a \n b")

  }
}
