package com.webfarm.Scorinator

import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import java.io.File


class NameScorer(nameInput: Either[File,List[String]], scoreFn: (String, Int) => Int ) {

}

object NameScorer {
  val logger = LoggerFactory.getLogger(classOf[NameScorer])

  lazy val singleNameConfig = ConfigFactory.load("scorinator.single-name")
  lazy val fmlConfig = ConfigFactory.load("scorinator.last-first-middle")
  lazy val lfmConfig = ConfigFactory.load("first-middle-last")



  def simpleScore: (String, Int) => Int = (name: String, sortPosition: Int) => {
    val chars = name.toUpperCase.toList.map(c => (c.toInt - 64))
    chars.sum * sortPosition
  }
}
