abstract class Person(name:String, surname:String)

case class Student(name : String, surname : String , id:Int) extends Person(name, surname)
case class Worker(name : String, surname : String , function:String)
  extends Person(name, surname)

val me = Student("Aymen", "Moussi", 23)
val worker = new Worker("Aymen", "Moussi", "Dev")

def getFullId[T <: Person](something: T) = {
  something match {
    case Student(name, surname, id) => s"$name-$surname-$id"
    case Worker(name, surname, function) => s"$name-$surname-$function"
  }
}

getFullId(me)
getFullId(worker)
