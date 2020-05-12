package com.webfarm.Scorinator.tests

import org.scalatest.flatspec.AnyFlatSpec
import com.webfarm.Scorinator._
import org.slf4j.LoggerFactory
import java.io.File

class NameScorerSpec extends AnyFlatSpec {

  import NameScorerSpec._


  s""""${linda} in sort position ${lindaOrder}"""" should s"receive a score of ${lindaScore}" in {
    assert(NameScorer.simpleScore(linda, lindaOrder) == lindaScore)
  }

  s""""${linda}"""" should s"""be at sort position ${lindaOrder} (array index ${lindaOrder - 1}) in the test array""" in {
    val sorted = names.sorted
    assert(linda.equals(sorted(lindaOrder - 1)))
  }

  s"""the total score for the test list""" should f"""be $namesScore%,d""" in {
    val sorted = names.sorted
    val nameScores =  1 to sorted.length map  ( idx => NameScorer.simpleScore(sorted(idx - 1 ), idx))
    assert(nameScores.sum == namesScore)
  }

  s"""creating a NameScorer with a list of strings""" should f"succeed and have a score4Names of $namesScore%,d" in {
    val scorer = new NameScorer(Right(names), NameScorer.simpleScore)
    assert(scorer.score4Names == namesScore)
  }

  "creating a NameScorer with a File" should f"succeed and have a score4Names of $namesFileScore%,d" in {
    val testfile = getClass().getResource("/names.txt")
    val scorer = new NameScorer(Left(new File(testfile.toURI)), NameScorer.simpleScore)
    assert(scorer.score4Names == namesFileScore)
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
