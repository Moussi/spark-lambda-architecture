package batch

import config.Settings
import domain._
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by moussi on 24/02/18.
  */
object DataFramesBatchJob {
  def main(args: Array[String]): Unit = {

    /**
      * get Spark configuration
      * set cluster manager we are you using local
      */
    val conf = new SparkConf().setAppName("Lambda with spark").setMaster("local[*]")
    val wlc = Settings.WebLogGen

    /**
      * instantiate Spark Context
      */
    val sc = new SparkContext(conf)
    /**
      * In order use Data frames we need to add spark sql context
      */
    val sqlContext= new SQLContext(sc)

    val filePath = wlc.filePath

    /**
      * Create RDD from data.tsv file that we genrated with LogProducer class
      */
    val sourceRDD = sc.textFile(filePath)
    val inputRDD = sourceRDD.flatMap( line => {
      buildActivityFromLine(line)
    })

    /**
      * create an RDD with composite Key (product:String, Long) and Value activity
      * Inout = Activity => output ((product, timestamp), activity)
      * Cache this RDD I will use it later
      */
    val keyedProducts = inputRDD.keyBy( input => (input.product, input.timestamp_hour)).cache()
    /**
      * map Values of Tuple keyed
      * MapValue keep the same key and map the value
      */
    val visitorsByProduct = keyedProducts.mapValues(activity => activity.visitor)
      .distinct().countByKey()


    /**
      * Reduce By Key foreach key we accumulate indices
      */
    val activityByProduct = keyedProducts.mapValues { activity =>
      activity.action match {
        case "purchase" => (1, 0, 0)
        case "add_to_cart" => (0, 1, 0)
        case "page_view" => (0, 0, 1)
      }
    }
      .reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2, a._3 + b._3))

    println("********* visitorsByProduct ***********")
    visitorsByProduct.foreach(println)
    println("********* activityByProduct ***********")
    activityByProduct.foreach(println)
  }
}
