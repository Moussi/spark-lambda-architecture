abstract class Person(name:String, surname:String)

case class Student(name : String, surname : String , id:Int) extends Person(name, surname)

implicit class StringWrapper(myString:String){

  def wordCount() = {
    val splited = myString.split("\\s+")
    val grouped = splited.groupBy(word => word)
    val count = grouped.mapValues(group => group.length)
    count
  }

  def upper() = {
    myString.toUpperCase
  }
}

implicit class IdIdentifier(person:Student){

  def getId() = {
    person.id
  }
}

"Moussi Aymen Moussi".wordCount()
"Moussi Aymen Moussi".upper()

val me = Student("Aymen", "Moussi", 23)
me.getId()
