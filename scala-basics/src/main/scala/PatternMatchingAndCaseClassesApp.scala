/**
  * Created by moussi on 13/05/18.
  */
object PatternMatchingAndCaseClassesApp {
  abstract class Notification
  case class Email(email:String, sender:String, receiver:String) extends Notification
  case class SMS(number:String, message:String) extends Notification
  case class CallReceiver(number:String) extends Notification

  def main(args: Array[String]): Unit = {
    val email = Email("hello", "moussi@gmail.com", "moussi2@gmail.com")
    val sms = SMS("07451212", "coucou")
    val call = CallReceiver("074512812")
    val senders = List("moussi@gmail.com", "moussi@hotmail.com")

    /**
      * Matching with Type
      */
    email match {
      case e:Email => println("You received an email")
      case s:SMS => println("You received a sms")
      case c:CallReceiver => println("You received a call")
    }
    /**
      * Matching with Type And Value
      */
    email match {
      case Email("hello", "moussi@gmail.com", _) => println(s"You received an email from ${email.sender}")
      case _ => println("wrong email")
    }

    email match {
      case Email("hello", "moussi@gmail.com", _) if(senders.contains(email.sender)) => println(s"You received an email from ${email.sender}")
      case _ => println("wrong email")
    }
  }

}
