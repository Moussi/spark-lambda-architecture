package batch

import domain._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SQLContext, SaveMode}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by moussi on 25/02/18.
  * Job to Be executed on YARN using spark-submit
  */
object HDFSBatchJob {
  def main(args: Array[String]): Unit = {

    /**
      * get Spark configuration
      * set cluster manager we are you using local
      */
    val conf = new SparkConf().setAppName("Lambda with spark").setMaster("local[*]")

    /**
      * instantiate Spark Context
      */
    val sc = new SparkContext(conf)
    /**
      * In order use Data frames we need to add spark sql context
      */
    val sqlContext= new SQLContext(sc)
    import sqlContext.implicits._

    val filePath = "file:///vagrant/data.tsv"

    /**
      * Create RDD from data.tsv file that we genrated with LogProducer class
      */
    val sourceRDD = sc.textFile(filePath)
    /**
      * we let the function defines the column names of our DF
      * because of using Activity case class
      */
    val inputDF = sourceRDD.flatMap( line => {
      buildActivityFromLine(line)
    }).toDF()

    /**
      * select rows/columns of our DF
      */
    val df = inputDF.select(
      add_months(from_unixtime(inputDF("timestamp_hour") / 1000), 1).as("timestamp_hour"),
      inputDF("referrer"), inputDF("action"), inputDF("prevPage"), inputDF("page"), inputDF("visitor"), inputDF("product")
    ).cache()

    df.registerTempTable("activity")

    /**
      * apply sql statement to our registeredTEmpTable
      */

    val visitorsByProduct = sqlContext.sql(
      """SELECT product, timestamp_hour, COUNT(DISTINCT visitor) as unique_visitors
        |FROM activity GROUP BY product, timestamp_hour
      """.stripMargin)

    visitorsByProduct.printSchema()
    //visitorsByProduct.foreach(println)

    val activityByProduct = sqlContext.sql("""SELECT
                                            product,
                                            timestamp_hour,
                                            sum(case when action = 'purchase' then 1 else 0 end) as purchase_count,
                                            sum(case when action = 'add_to_cart' then 1 else 0 end) as add_to_cart_count,
                                            sum(case when action = 'page_view' then 1 else 0 end) as page_view_count
                                            from activity
                                            group by product, timestamp_hour """).cache()

    activityByProduct.registerTempTable("activityProduct")

    /**
      * with partitionby spark will create each directory for each timestamp line
      */
    activityByProduct.write.partitionBy("timestamp_hour").mode(SaveMode.Append).parquet("hdfs://lambda-pluralsight:9000/lambda/batch1")

    /**
      * Now we need to make a fat jar an run this fat in jar in yarn using spark-submit
      * mvn clean package
      * copy fat jar into shared folder with VM machine
      * connect ssh to VM
      * and run spark-submit command to run our job in yarn executor
      * ./spark-submit --master yarn --deploy-mode cluster --class batch.HDFSBatchJob /folder
      *
      * /folder : folder where fat jar is located
      */
  }
}
