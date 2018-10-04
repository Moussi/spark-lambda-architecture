package utils

import config.Settings
import org.apache.spark.sql.SQLContext
import org.apache.spark.streaming.{Duration, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}


/**
  * Created by moussi on 28/02/18.
  */
object SparkUtils {

  val config = Settings.Configuration

  /**
    * return SparkContexts
    */
  def getSparkContext(appName: String) = {
    /**
      * get Spark configuration
      * set cluster manager we are you using local
      */
    val conf = new SparkConf().setAppName("Lambda with spark")
    var checkPointDirectory = "hdfs://lambda-pluralsight:9000/spark/checkpoint"
    if (config.local_deploy_mode) {
      conf.setMaster("local[*]")
      checkPointDirectory = "file:///home/amoussi/Desktop/projetcs/cassandra"
    }

    /**
      * instantiate Spark Context
      */
    val sc = SparkContext.getOrCreate(conf)
    sc.setCheckpointDir(checkPointDirectory)
    sc
  }

  /**
    * return SparkSqlContext
    */
  def getSparkCqlContext(sc: SparkContext) = {
    val sqlContext = SQLContext.getOrCreate(sc)
    sqlContext
  }

  /**
    * returns SparkStreaming
    */
  def getSparkStreamingContext(appStreaming: (SparkContext, Duration) => StreamingContext,
                               sc: SparkContext, microBatchingDuration: Duration) = {
    val creatingFunc = () => appStreaming(sc, microBatchingDuration)
    val ssc = sc.getCheckpointDir match {
      case Some(checkPointDirectory) => StreamingContext.getActiveOrCreate(checkPointDirectory, creatingFunc, sc.hadoopConfiguration, createOnError = false)
      case None => StreamingContext.getActiveOrCreate(creatingFunc)
    }
    sc.getCheckpointDir.foreach(cp => ssc.checkpoint(cp))
    ssc
  }

}
