package batch

import config.Settings
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by moussi on 24/02/18.
  */
object BatchJob {
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

    val filePath = wlc.filePath

    /**
      * Create RDD from data.tsv file that we genrated with LogProducer class
      */
    val sourceRDD = sc.textFile(filePath)
    sourceRDD.foreach(println)
  }
}
