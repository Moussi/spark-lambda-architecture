def sayHello(name: String)(whoAreyou: () => String) = {
  s"Hello $name!! My Name is ${whoAreyou()}"
}

def sayHello2(name: String)(implicit whoAreyou: () => String) = {
  s"Hello $name!! My Name is ${whoAreyou()}"
}

implicit def provideName() = {
  "Aymen"
}

implicit def provideName2() = {
  "Aymen2"
}

println(sayHello("moussi") { () => "FOulen" } )
println(sayHello2("moussi"))
