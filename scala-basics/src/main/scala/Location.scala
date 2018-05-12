/**
  * Created by moussi on 12/05/18.
  */
class Location(xc:Int, yc:Int, zc:Int) extends Point(xc,yc){
  var z = zc

  def move(dx:Int,dy:Int,dz:Int){
    move(dx,dy)
    z+=dz
    println("z = "+z)
  }

}
