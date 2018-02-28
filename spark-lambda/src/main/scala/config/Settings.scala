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
    private val config = conf.getConfig("commons")

    /**
      * the reason I'm using lazy vals here is that I don't want Scala to evaluate the value of these properties immediately.
        I only want it to evaluate the value of the parameter when it gets used.
      */
    lazy val records = config.getInt("records")
    lazy val timeMultiplier = config.getInt("time_multiplier")
    lazy val pages = config.getInt("pages")
    lazy val visitors = config.getInt("visitors")
    lazy val filePath = config.getString("file_path")
    lazy val destPath = config.getString("dest_path")
    lazy val local_deploy_mode = config.getBoolean("local_deploy_mode")
    lazy val filesNumber = config.getInt("number_of_files")
  }
}
