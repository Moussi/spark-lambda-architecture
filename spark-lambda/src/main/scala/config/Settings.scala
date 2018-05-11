package config

import com.typesafe.config.ConfigFactory

/**
  * Created by moussi on 24/02/18.
  * scala object guarantee a single instance of this class
  * Singleton
  */
object Settings {
  /**
    * TypeSafe Config library
    */
  private val conf = ConfigFactory.load()

  object Configuration {
    private val commonsConfig = conf.getConfig("commons")
    private val kafkaConfig = conf.getConfig("kafka")

    /**
      * the reason I'm using lazy vals here is that I don't want Scala to evaluate the value of these properties immediately.
        I only want it to evaluate the value of the parameter when it gets used.
      */
    lazy val records = commonsConfig.getInt("records")
    lazy val timeMultiplier = commonsConfig.getInt("time_multiplier")
    lazy val pages = commonsConfig.getInt("pages")
    lazy val visitors = commonsConfig.getInt("visitors")
    lazy val filePath = commonsConfig.getString("file_path")
    lazy val destPath = commonsConfig.getString("dest_path")
    lazy val local_deploy_mode = commonsConfig.getBoolean("local_deploy_mode")
    lazy val filesNumber = commonsConfig.getInt("number_of_files")
    /**
      * Kafka Configuration
      */
    lazy val weblogTopic = kafkaConfig.getString("kafka_weblog_topic")
    lazy val kafkaHosts = kafkaConfig.getString("kafka_hosts")
    lazy val keyStringSerializer = kafkaConfig.getString("kafka_key_serializer")
    lazy val valueStringSerializer = kafkaConfig.getString("kafka_value_serializer")
    lazy val kafkaProducerAcks = kafkaConfig.getString("kafka_producer_acks")
    lazy val kafkaProducerClientId = kafkaConfig.getString("kafka_producer_client_id")
  }
}
