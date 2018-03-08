# load data from hdfs 
```
val parquetDF = sqlContext.read.parquet("hdfs://localhost:9000/lambda/batch1")
parquetDF.show()
```
# load using sql 
```
val parquetDF = sqlContext.read.parquet("hdfs://localhost:9000/lambda/batch1")
parquetDF.show()
```
# Streaming Aggregations 

## declare case classes

```
case class Activity(timestamp_hour: Long,
                      referrer: String,
                      action: String,
                      prevPage: String,
                      page: String,
                      visitor: String,
                      product: String,
                      inputProps: Map[String, String] = Map()
                     )
                    
case class ActivityByProduct(
                  product: String,
                  timestamp_hour: Long,
                  purchaseCount: Long,
                  addToCardCount: Long,
                  pageViewCount: Long
                 )
```

## create SparkStreamingContext
The ```@transient``` keyword in Scala marks a field or value such that it doesn't serialize it in memory and 
transfer it over the network. In Spark terms what this really means is that when you try to access this 
object on a worker node, the object ends up being created on the worker nodes themselves instead of actually 
being created on the driver and the serialized and then passed along to the workers. 
So instead of the driver creating the object and passing it along to the workers, the workers themselves 
end up instantiating the object. And the reason we're doing this here specifically in Zeppelin for the 
SparkStreamingContext is that Zeppelin will try and serialize the SparkStreamingContext because it needs it 
in other locations. And to do that, it needs to grab the surrounding closure. 
And in Zeppelin's case, the surrounding closure's context is everything in the notebook. 
So that means that Zeppelin will eventually try to serialize everything just to pass around the SparkStreamingContext. 
This is a little bit more involved than I wanted to get into here, but just understand that this is a special 
case for Zeppelin that we have to create the SparkStreamingContext as a transient variable so that it doesn't 
attempt to serialize it along with everything else. If you don't do this this way, you'll actually get 
serialization exceptions. 

```
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}
@transient val ssc = new StreamingContext(sc, Seconds(4))
ssc.checkpoint("hdfs://localhost:9000/spark/checkpoint")
```

## Exploring input and manage streaming
```
val inputPath = "file:///vagrant/input"

      /**
        *  monitor a single directory from new files and then consume the data from that file
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

      activityDStream.foreachRDD(rdd => {
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
                                            group by product, timestamp_hour """)

activityByProduct.registerTempTable("activityProduct")
      })
      
ssc.start()
```

## SqlContext
```
%sql
select
    from_unixtime(timestamp_hour/1000, "MM-dd HH:mm:00") as timestamphour,
    purchase_count,
    add_to_cart_count,
    page_view_count
from  
    activityProduct
```

## close StreamingContext  
ow we're almost ready to run this example, but one last note in that how you can stop your
SparkStreamingContext without affecting the status of your Zeppelin notebook. 
And the way to do this correctly is to use the StreamingContext.getActive, which returns a list of the
active StreamingContext, essentially in this case only 1, and then loop against those with foreach,
essentially every StreamingContext will be represented by this placeholder underscore. 
And then we'll call stop on each StreamingContext. However, we want to make sure that the Zeppelin
SparkContext doesn't get stopped along with the StreamingContext. 
Remember, Zeppelin actually brings up its own SparkContext, so we don't want to stop that. 
And so we call this stop method with the stopSparkContext set to false and stopGracefully set to true. 
And then, finally, you can choose to use a ssc.awaitTerminationOrTimeout. 

```
StreamingContext.getActive.foreach{_.stop(stopSparkContext=false, stopGracefully=true) }
ssc.awaitTerminationOrTimeout(1000*6)
```

