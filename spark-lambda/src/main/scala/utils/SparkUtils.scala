package utils

import config.Settings
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by moussi on 28/02/18.
  */
object SparkUtils {

  val config = Settings.Configuration

  def getSparkContext(appName:String) : SparkContext = {
    /**
      * get Spark configuration
      * set cluster manager we are you using local
      */
    val conf = new SparkConf().setAppName("Lambda with spark")
    var checkPointDirectory = "hdfs://lambda-pluralsight:9000/spark/checkpoint"
    if (config.local_deploy_mode) {
      conf.setMaster("local[*]")
      checkPointDirectory = "file:///home/moussi/Desktop/Projects/LamdaArchitecture/Boxes/spark-kafka-cassandra-applying-lambda-architecture/vagrant/lambda"
    }

    /**
      * instantiate Spark Context
      */
    val sc = SparkContext.getOrCreate(conf)
    sc.setCheckpointDir(checkPointDirectory)
    sc
  }

  def getSparkCqlContext(sc: SparkContext) : SQLContext = {
    val sqlContext= SQLContext.getOrCreate(sc)
    sqlContext
  }

}
