# load data from hdfs 
val parquetDF = sqlContext.read.parquet("hdfs://lambda-pluralsight:9000/lambda/batch1")
parquetDF.show()
# load using sql 
val parquetDF = sqlContext.read.parquet("hdfs://lambda-pluralsight:9000/lambda/batch1")
parquetDF.show()
