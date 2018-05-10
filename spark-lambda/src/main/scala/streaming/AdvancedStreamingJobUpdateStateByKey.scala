package streaming

import config.Settings
import domain._
import org.apache.spark.SparkContext
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}
import utils.SparkUtils._
import _root_.config.Settings.Configuration.local_deploy_mode


/**
  * Created by moussi on 28/02/18.
  */
object AdvancedStreamingJobUpdateStateByKey {

  /**
    * Define MicroBatching period
    */
  var microBatchDuration = Seconds(4)
  /**
    * Load Settings from application.conf file
    */
  val settings = Settings.Configuration

  def main(args: Array[String]): Unit = {
    /**
      * load Streaming Spark Context
      */
    val sc = getSparkContext("Lambda App with Streaming")
    val sqlContext = getSparkCqlContext(sc)
    import sqlContext.implicits._
    /**
      * Create Spark Streaming Context
      */
    def streamingApp(sc: SparkContext, microBatchDuration: Duration): StreamingContext = {
      val ssc = new StreamingContext(sc, microBatchDuration)
      /**
        * define input path in case of local or cluster deployment
        */
      val inputPath = local_deploy_mode match {
        case true => "file:///home/moussi/Desktop/Projects/LamdaArchitecture/Boxes/spark-kafka-cassandra-applying-lambda-architecture/vagrant/input"
        case false => "file:///vagrant/input"
      }

      /**
        * monitor a single directory from new files and then consume the data from that file
        */
      val textDstream = ssc.textFileStream(inputPath)
      /**
        * print streamed file
        */
      val activityDStream = textDstream.transform(rdd =>
        rdd.flatMap { line =>
          buildActivityFromLine(line)
        }
      )

      val statefullActivityByProduct = activityDStream.transform(rdd => {
        val df = rdd.toDF
        df.registerTempTable("activity")
        val activityByProduct = sqlContext.sql(
          """SELECT
                                            product,
                                            timestamp_hour,
                                            sum(case when action = 'purchase' then 1 else 0 end) as purchase_count,
                                            sum(case when action = 'add_to_cart' then 1 else 0 end) as add_to_cart_count,
                                            sum(case when action = 'page_view' then 1 else 0 end) as page_view_count
                                            from activity
                                            group by product, timestamp_hour """).cache()

        activityByProduct.registerTempTable("activityProduct")
        activityByProduct.map { r =>
          ((r.getString(0), r.getLong(1)),
            ActivityByProduct(r.getString(0), r.getLong(1), r.getLong(2), r.getLong(3), r.getLong(4)))
        }
      }).updateStateByKey((newItemByKey: Seq[ActivityByProduct],
                           currentState: Option[(Long, Long, Long, Long)])
      => updateActivityByProductState(newItemByKey, currentState))

      statefullActivityByProduct.print(10)
      ssc
    }

    val ssc = getSparkStreamingContext(streamingApp, sc, microBatchDuration)
    /**
      * start scc
      */
    ssc.start()
    ssc.awaitTermination()
  }

  private def updateActivityByProductState(newItemByKey: Seq[ActivityByProduct], currentState: Option[(Long, Long, Long, Long)]) = {


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
