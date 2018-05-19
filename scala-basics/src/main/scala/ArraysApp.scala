import scala.Array._

/**
  * Created by moussi on 12/05/18.
  */
object ArraysApp {
  def main(args: Array[String]): Unit = {
    val tab = Array(2, 3 ,4)
    val tab2 = new Array[String](2)
    tab2(0) = "Moussi"
    tab2(1) = "Aymen"

    for(elem <- tab){
      println(elem)
    }

    for(elem <- tab2){
      println(elem)
    }
    /**
      * Matrix
      */
    var mat = ofDim[Int](3,3)

    for(i <- 0 to 2){
      for(j <- 0 to 2){
        mat(i)(j) = j
      }
    }

    // Print two dimensional array
    for (i <- 0 to 2) {
      for ( j <- 0 to 2) {
        print(" " + mat(i)(j));
      }
      println();
    }
  }
}
