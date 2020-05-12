package com.webfarm.Scorinator

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger

class NameScorer() {

}

object NameScorer {
  val logger = Logger(classOf[NameScorer])

  lazy val singleNameConfig = ConfigFactory.load("scorinator.single-name")
  lazy val fmlConfig = ConfigFactory.load("scorinator.last-first-middle")
  lazy val lfmConfig = ConfigFactory.load("first-middle-last")
}
