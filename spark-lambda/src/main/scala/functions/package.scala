/**
  * Created by moussi on 11/05/18.
  */

import com.twitter.algebird.{HLL, HyperLogLogMonoid}
import domain._
import org.apache.spark.streaming.State
import org.joda.time.LocalDate

package object functions {

  def mapActivityStateFunc = (k: (String, Long), v: Option[ActivityByProduct], state: State[(Long, Long, Long)]) => {
    var (purchase_count, add_to_cart_count, page_view_count) = state.getOption().getOrElse(0L, 0L, 0L)

    val value = v match {
      case Some(a: ActivityByProduct) => (a.purchaseCount, a.addToCardCount, a.pageViewCount)
      case _ => (0L, 0L, 0L)
    }

    purchase_count += value._1
    add_to_cart_count += value._2
    page_view_count += value._3
    state.update(purchase_count, add_to_cart_count, page_view_count)

    val underExposed = {
      if (purchase_count == 0)
        0L
      else
        page_view_count / purchase_count
    }
    underExposed
  }

  def mapVisitorsStateFunc = (k: (String, Long), v: Option[HLL], state: State[HLL]) => {
    val currentVisitorHLL = state.getOption().getOrElse(new HyperLogLogMonoid(12).zero)

    val newVisitorHLL = v match {
      case Some(visitorHLL) => currentVisitorHLL + visitorHLL
      case None => currentVisitorHLL
    }

    state.update(newVisitorHLL)

    val output = newVisitorHLL.approximateSize.estimate
    output
  }

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

  def buildAudienceFromLine(line:String): Option[AudiencePoi] = {
    val record = line.split("\\t")
    println(line)
    val day = LocalDate.parse(record(0)).dayOfMonth()
    val month = LocalDate.parse(record(0)).monthOfYear()
    val year = LocalDate.parse(record(0)).year()
    None
    if (record.length == 7)
      Some(AudiencePoi(nb = record(0), poi_id = record(1)))
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
