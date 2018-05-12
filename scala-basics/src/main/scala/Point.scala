
/**
  * Created by moussi on 12/05/18.
  */
class Point(xc: Int, yc:Int) {
  var x = xc;
  var y = yc;

  def move(dx:Int, dy:Int){
    x+=dx
    y+=dy
    println("x = "+x)
    println("y = "+y)
  }
}
