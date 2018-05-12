package kafka

import java.util.Properties

import config.Settings
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerConfig, ProducerRecord}

import scala.util.Random

/**
  * Created by moussi on 24/02/18.
  * Extending from the App scala class is a quick Scala shortcut to have the class LogProducer
  * become runnable without having to define a main function. So essentially the body of the class now becomes
  * your main executable
  */
object KafkaLogProducer extends App{

  // Load config
  val config = Settings.Configuration

  val Products = scala.io.Source.fromInputStream(getClass.getResourceAsStream("/products.csv")).getLines().toArray
  val Referrers = scala.io.Source.fromInputStream(getClass.getResourceAsStream("/referrers.csv")).getLines().toArray
  val Visitors = (0 to config.visitors).map("Visitor-" + _)
  val Pages = (0 to config.pages).map("Page-" + _)

  val rnd = new Random()
  val filePath = config.filePath
  val destPath = config.destPath

  val topic = config.weblogTopic

  val props = new Properties()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafkaHosts)
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, config.keyStringSerializer)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, config.valueStringSerializer)
  props.put(ProducerConfig.ACKS_CONFIG, config.kafkaProducerAcks)
  props.put(ProducerConfig.CLIENT_ID_CONFIG, config.kafkaProducerClientId)

  val kafkaProducer:Producer[Nothing, String] = new KafkaProducer[Nothing, String](props)

  for (fileCount <- 1 to config.filesNumber) {

    //val fw = new FileWriter(filePath, true)

    // introduce some randomness to time increments for demo purposes
    val incrementTimeEvery = rnd.nextInt(config.records - 1) + 1

    var timestamp = System.currentTimeMillis()
    var adjustedTimestamp = timestamp

    for (iteration <- 1 to config.records) {
      adjustedTimestamp = adjustedTimestamp + ((System.currentTimeMillis() - timestamp) * config.timeMultiplier)
      timestamp = System.currentTimeMillis() // move all this to a function
      val action = iteration % (rnd.nextInt(200) + 1) match {
        case 0 => "purchase"
        case 1 => "add_to_cart"
        case _ => "page_view"
      }
      val referrer = Referrers(rnd.nextInt(Referrers.length - 1))
      val prevPage = referrer match {
        case "Internal" => Pages(rnd.nextInt(Pages.length - 1))
        case _ => ""
      }
      val visitor = Visitors(rnd.nextInt(Visitors.length - 1))
      val page = Pages(rnd.nextInt(Pages.length - 1))
      val product = Products(rnd.nextInt(Products.length - 1))

      val line = s"$adjustedTimestamp\t$referrer\t$action\t$prevPage\t$visitor\t$page\t$product\n"
      val producerRecord = new ProducerRecord(topic, line)
      kafkaProducer.send(producerRecord)
      //fw.write(line)

      if (iteration % incrementTimeEvery == 0) {
        println(s"Sent $iteration messages!")
        val sleeping = rnd.nextInt(incrementTimeEvery * 60)
        println(s"Sleeping for $sleeping ms")
        Thread sleep sleeping
      }

    }

    val sleeping = 2000
    println(s"Sleeping for $sleeping ms")
  }

  kafkaProducer.close()
}
