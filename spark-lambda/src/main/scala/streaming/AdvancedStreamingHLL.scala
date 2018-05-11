package streaming

import com.twitter.algebird.HyperLogLogMonoid
import config.Settings
import domain._
import functions._
import org.apache.spark.SparkContext
import org.apache.spark.streaming._
import utils.SparkUtils._


/**
  * Created by moussi on 28/02/18.
  */
object AdvancedStreamingHLL {

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
      val inputPath = if (settings.local_deploy_mode) {
        "file:///home/moussi/Desktop/Projects/LamdaArchitecture/Boxes/spark-kafka-cassandra-applying-lambda-architecture/vagrant/input"
      } else {
        "file:///vagrant/input"
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

      val activityStateSpec = StateSpec.function(mapActivityStateFunc)
        .timeout(Seconds(30L))

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
      }).mapWithState(activityStateSpec)

      val activityByProdcutStateSnapshot = statefullActivityByProduct.stateSnapshots();
      activityByProdcutStateSnapshot.

        /** reduceByKeyAndWindow is used here to avoid iterating the hole state
          * (a,b) a the current state, b the old state
          * (x,y) x the current state, y the old state (reverse function of reduceByKeyAndWindow)
          */
        reduceByKeyAndWindow((a, b) => b,
        (x, y) => x,
        Seconds(30 / 4 * 4))
        .foreachRDD(rdd =>
          rdd.map(ac => ActivityByProduct(ac._1._1, ac._1._2, ac._2._1, ac._2._2, ac._2._3))
            .toDF().registerTempTable("activityByProduct"))

      // unique visitors by product
      val visitorsByProductSpec = StateSpec.function(mapVisitorsStateFunc)
        .timeout(Minutes(120))
      val hll = new HyperLogLogMonoid(12)

      val statefulVisitorsByProduct = activityDStream.map(
        a => ((a.product, a.timestamp_hour), hll(a.visitor.getBytes())))
        .mapWithState(visitorsByProductSpec)
      val visitorsByProdcutStateSnapshot = statefulVisitorsByProduct.stateSnapshots();
      visitorsByProdcutStateSnapshot.

        /** reduceByKeyAndWindow is used here to avoid iterating the hole state
          * (a,b) a the current state, b the old state
          * (x,y) x the current state, y the old state (reverse function of reduceByKeyAndWindow)
          */
        reduceByKeyAndWindow((a, b) => b,
                             (x, y) => x,
                              Seconds(30 / 4 * 4))
        .foreachRDD(rdd =>
          rdd.map(vp => VisitorsByProduct(vp._1._1, vp._1._2, vp._2.approximateSize.estimate))
            .toDF().registerTempTable("visitorsByProduct"))

      ssc
    }


    val ssc = getSparkStreamingContext(streamingApp, sc, microBatchDuration)
    /**
      * start scc
      */
    ssc.start()
    ssc.awaitTermination()
  }
}
