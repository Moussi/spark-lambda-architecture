package functions

import scala.util.{Failure, Success, Try}

/**
  * Created by moussi on 30/05/18.
  */
object Exceptions extends App{
  val a = 5;
  val b= 1;
  val outcome = Try(a/b)

  if(outcome.isSuccess) println("Yeahhhh") else println("Noooooooooooo")

  outcome match {
    case Success(value) => println("sucsess "+value)
    case Failure(e) => println("Faillllll "+e)
  }
}
