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

  object WebLogGen {
    private val webLogGen = conf.getConfig("clickstream")

    /**
      * the reason I'm using lazy vals here is that I don't want Scala to evaluate the value of these properties immediately.
        I only want it to evaluate the value of the parameter when it gets used.
      */
    lazy val records = webLogGen.getInt("records")
    lazy val timeMultiplier = webLogGen.getInt("time_multiplier")
    lazy val pages = webLogGen.getInt("pages")
    lazy val visitors = webLogGen.getInt("visitors")
    lazy val filePath = webLogGen.getString("file_path")
  }
}
