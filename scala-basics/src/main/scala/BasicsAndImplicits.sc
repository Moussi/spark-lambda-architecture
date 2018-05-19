def sayHello(name: String)(whoAreyou: () => String) = {
  s"Hello $name!! My Name is ${whoAreyou()}"
}

def sayHello2(name: String)(implicit whoAreyou: () => String) = s"Hello $name!! My Name is ${whoAreyou()}"

implicit def provideName() = "Aymen"

println(sayHello("moussi") { () => "FOulen" } )
println(sayHello2("moussi"))

val list = List(List("Aymen"), List("Moussi"))
val flattenList = list.flatten
println(flattenList)
