package streaming

import config.Settings
import org.apache.spark.SparkContext
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}
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
  val settings = Settings.Configuration

  def main(args: Array[String]): Unit = {
    /**
      * load Streaming Spark Context
      */
    val sc = getSparkContext("Lambda App with Streaming")
    /**
      * Create Spark Streaming Context
      */
    def streamingApp(sc: SparkContext, microBatchDuration: Duration) : StreamingContext= {
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
