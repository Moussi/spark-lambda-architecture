import scala.util.Random

/**
  * Created by moussi on 13/05/18.
  */
object ExtractorsApp {

  case class CustomerID(name: String) {

    def apply(name: String): CustomerID = new CustomerID(s"$name--${Random.nextLong()}")

    def unapply(customerID: CustomerID): Option[String] = {
      if (isBlank(Option(customerID.name)))
        Some(name)
      else
        None

    }

    def isBlank(s: Option[String]) = {
      s match {
        case Some(s) => s.trim.isEmpty
        case None => true
      }
    }
  }

  def main(args: Array[String]): Unit = {
val customerID = CustomerID("moussi")
    customerID match{
      case CustomerID("") => println(customerID.name)
      case _ => println("Could not extract a CustomerID")
    }
  }
}
