/**
  * Created by moussi on 12/05/18.
  */
object StringUtils{

  implicit class StringImplicits(s: String) {
    private[StringUtils] var x = "sss"
    def upper() = s.toUpperCase
    def addSymbolAtTheEnd(symbol:Char) = s + symbol
    def sayHello(implicit whoAreYou: () => String) = s"Hello $whoAreYou"
  }

}
