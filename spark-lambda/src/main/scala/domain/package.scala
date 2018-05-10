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

  case class ActivityByProduct(
                      product: String,
                      timestamp_hour: Long,
                      purchaseCount: Long,
                      addToCardCount: Long,
                      pageViewCount: Long
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

  def updateActivityByProductState(newItemByKey: Seq[ActivityByProduct], currentState: Option[(Long, Long, Long, Long)]) = {


    var (previousTimeStamp, purchase_count, add_to_cart_count, page_view_count) = currentState.getOrElse(System.currentTimeMillis(), 0L, 0L, 0L)

    var result: Option[(Long, Long, Long, Long)] = null

    if (newItemByKey.isEmpty) {
      if (System.currentTimeMillis() - previousTimeStamp > 30000 + 4000) {
        result = None
      } else {
        result = Some((previousTimeStamp, purchase_count, add_to_cart_count, page_view_count))
      }
    } else {
      newItemByKey.foreach(a => {
        purchase_count += a.purchaseCount
        add_to_cart_count += a.addToCardCount
        page_view_count += a.pageViewCount
      })
      result = Some((System.currentTimeMillis(), purchase_count, add_to_cart_count, page_view_count))
    }

    result
  }

}
