package exceptions

import scala.util.{Failure, Success, Try}

/**
  * Created by amoussi on 25/06/18.
  */
object ExceptionsMain extends App{

  def getResult(name: String) = Try {
    if (name.contains("wow")) Success(name) else Failure(new Exception("sorry"))
  }

  val result = getResult("dfsd")

  result match {
    case Success(value) => println(value.get)
    case Failure(e) => {
      println(s"This is an exception ${e}")
    }
  }

}
