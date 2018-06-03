package patternmatching

import java.util.Date

import scala.util.Random

/**
  * Created by moussi on 03/06/18.
  */
object Main extends App {

  /**
    * Match on constants
    */
  val number = 99;
  val numberInLetters = number match {
    case 0 => "zero"
    case 5 => "five"
    case 9 => "nine"
    case _ => "Other"
  }

  println(numberInLetters)

  /**
    * Match
    * on case Classes
    */
  case class Book(name: String, author: String, isbn: String)

  val wtfBook = Book("WTF Book", "Aymen Moussi", "1542")

  val result = wtfBook match {
    case Book(name, author, isbn) => s"This an amazing book named $name author : $author"
    case _ => "Unkonwn book"
  }
  println(result)

  val result2 = wtfBook match {
    case Book(_, author, _) => s"The author of this book : $author"
    case _ => "Unkonwn book"
  }
  println(result2)

  /**
    * Match On seq
    */
  val list = List(1, 12, 15, 17)

  val result3 = list match {
    case List(_, second, _*) => s"this the second element of the list : $second"
    case _ => "Unmatched list"
  }

  println(result3)

  /**
    * Match on Type
    */
  case class Trip(to: String)

  case class Travel(to: String, date: Date)

  case class Interview(to: String, date: Date, post: String)

  val trip = Trip("Paris")
  val travel = Travel("Tunis", new Date())
  val interview = Interview("Google", new Date(), "Manager")
  val listOfTypes = trip :: travel :: interview :: trip :: trip :: travel :: Nil

  val matchType = Random.shuffle(listOfTypes).take(1)(0) match {
    case t: Trip => s"Trip to : ${t.to}"
    case tr: Travel => s"Travel to : ${tr.to} on : ${tr.date}"
    case intvw: Interview => s"Inteview in : ${intvw.to} date : ${intvw.date} for post : ${intvw.post}"
  }
  println(matchType)

  /**
    * Guard Matches
    */
  val trip2 = Trip("Rome")
  val trip3 = Trip("Nice")
  val trips = trip::trip2::trip3::Nil
  val result4 = Random.shuffle(listOfTypes).take(1)(0) match {
    case Trip(to) if to == "Paris" => s"Trip to Paris"
    case _ => s"No trips to Paris"
  }
  println(result4)

}
