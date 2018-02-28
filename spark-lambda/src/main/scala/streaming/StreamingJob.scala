package streaming

import config.Settings
import org.apache.spark.streaming.{Seconds, StreamingContext}
import utils.SparkUtils._

/**
  * Created by moussi on 28/02/18.
  */
object StreamingJob {

  /**
    * Define MicroBatching period
    */
  var microBatchDuration = Seconds(4)
  /**
    * Load Settings from application.conf file
    */
  val settings = Settings.WebLogGen

  def main(args: Array[String]): Unit = {
    /**
      * load Spark Context
      */
    val sc = getSparkContext("Lambda App with Streaming")
    /**
      * Create Spark Streaming Context
      */
    val ssc = new StreamingContext(sc, microBatchDuration)

    /**
      * define input path in case of local or cluster deployment
      */
    val inputPath = settings.local_deploy_mode match {
      case true => "file:///home/moussi/Desktop/Projects/LamdaArchitecture/Boxes/spark-kafka-cassandra-applying-lambda-architecture/vagrant/input"
      case false => "file:///vagrant/input"
    }

    /**
      *  monitor a single directory from new files and then consume the data from that file
      */
    val textDstream = ssc.textFileStream(inputPath)
    /**
      * print streamed file
      */
    textDstream.print()

    /**
      * start scc
      */
    ssc.start()
    ssc.awaitTermination()
  }
}
