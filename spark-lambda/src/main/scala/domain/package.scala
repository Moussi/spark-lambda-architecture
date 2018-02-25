/**
  * Created by moussi on 24/02/18.
  */
package object domain {
  case class Activity(timestamp_hour: Long,
                      referrer: String,
                      action: String,
                      prevPage: String,
                      page: String,
                      visitor: String,
                      product: String,
                      inputProps: Map[String, String] = Map()
                     )

  /**
    * static method like in java
    * @param line
    * @return
    */
  def buildActivityFromLine(line:String): Option[Activity] = {
    val record = line.split("\\t")
    val MS_IN_HOUR = 60 * 60 * 1000
    if (record.length == 7)
      Some(Activity(record(0).toLong / MS_IN_HOUR * MS_IN_HOUR, record(1),
        record(2), record(3), record(4), record(5), record(6)))
    else
      None
  }

}
