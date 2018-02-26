package batch

import config.Settings
import domain._
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.functions._
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by moussi on 24/02/18.
  *   * Job Using Local files and running on local cluster
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
    import sqlContext.implicits._

    val filePath = wlc.filePath

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
      * Define a custom function via sqlContext and used with you sql statement
      *
      */

    sqlContext.udf.register("UnderExposed", (pageViewCount: Long, purshaseCount: Long) => if (purshaseCount == 0) 0 else pageViewCount/purshaseCount)
    val underExposedProducts = sqlContext.sql("""SELECT
                                            product,
                                            timestamp_hour,
                                            UnderExposed(page_view_count, purchase_count) as negative_exposure
                                            from activityProduct
                                            ORDER BY negative_exposure
                                            LIMIT 5 """)
    underExposedProducts.printSchema()
    underExposedProducts.foreach(println)

  }
}
