package batch

import domain.AudiencePoi
import utils.SparkUtils._

/**
  * Created by moussi on 25/02/18.
  * Job to Be executed on YARN using spark-submit
  */
object AudiencePoiBatchJob {
  def main(args: Array[String]): Unit = {

    /**
      * get Spark configuration
      * set cluster manager we are you using local
      * instantiate Spark Context
      */
    val sc = getSparkContext("Lambda App Hdfs")
    /**
      * In order use Data frames we need to add spark sql context
      */
    val sqlContext= getSparkCqlContext(sc)

    val filePath = "file:////mnt/hadoop/hive-warehouse/bi_poi_audience/year=2018/month=10/day=3/000006_0"

    /**
      * Create RDD from data.tsv file that we genrated with LogProducer class
      */
    val sourceRDD = sc.textFile(filePath)


    println("qscsqdcsdcsqc    " + sourceRDD.count())
    /**
      * we let the function defines the column names of our DF
      * because of using Activity case class
      */
    val audienceDF = sourceRDD.map( line =>
      {
        val record = line.split("\\t")
        AudiencePoi(nb = record(0), poi_id = record(1))
      })

    audienceDF.take(1).foreach( audience => println("audiencePoi"+ audience.poi_id))

    println("Audience process finished !!!!!!  "+ audienceDF.count())

  }
}
