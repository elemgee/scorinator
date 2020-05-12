package com.webfarm.Scorinator.tests

import org.scalatest.flatspec.AnyFlatSpec
import com.webfarm.Scorinator._
import org.slf4j.LoggerFactory

class NameScorerSpec extends AnyFlatSpec {

  import NameScorerSpec._


  s""""${linda} in sort position ${lindaOrder}"""" should s"receive a score of ${lindaScore}" in {
    assert(NameScorer.simpleScore(linda, lindaOrder) == lindaScore)
  }

  s""""${linda}"""" should s"""be at sort position ${lindaOrder} (array index ${lindaOrder - 1}) in the test array""" in {
    val sorted = names.sorted
    assert(linda.equals(sorted(lindaOrder - 1)))
  }

  s"""the total score for the test list""" should s"""be ${namesScore}""" in {
    val sorted = names.sorted
    val nameScores =  1 to sorted.length map  ( idx => NameScorer.simpleScore(sorted(idx - 1 ), idx))
    assert(nameScores.sum == namesScore)
  }

}

object NameScorerSpec {

  val logger = LoggerFactory.getLogger(classOf[NameScorerSpec])
  val names = List("MARY", "PATRICIA", "LINDA", "BARBARA", "VINCENZO", "SHON", "LYNWOOD", "JERE", "HAI")
  val linda = "LINDA"
  val lindaOrder = 4
  val lindaScore = 160
  val namesScore = 3194

  val namesFileScore = 871198282

}
