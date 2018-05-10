/**
  * Created by moussi on 11/05/18.
  */

import domain._
import org.apache.spark.streaming.State

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
}
