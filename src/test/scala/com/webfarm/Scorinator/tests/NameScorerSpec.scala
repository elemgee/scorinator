package com.webfarm.Scorinator.tests



import org.slf4j.LoggerFactory
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.funspec.AnyFunSpec

import java.io.File
import com.webfarm.Scorinator._

class NameScorerSpec extends AnyFunSuite {

  import NameScorerSpec._


  test(s""""${linda}" in sort position ${lindaOrder} should receive a score of ${lindaScore}""") {
    assert(NameScorer.simpleScore(linda, lindaOrder) == lindaScore)
  }

 test(s""""${linda}" should be at sort position ${lindaOrder} (array index ${lindaOrder - 1}) in the test array"""){
    val sorted = names.sorted
    assert(linda.equals(sorted(lindaOrder - 1)))
  }

  test(f"""the total score for the test list be $namesScore%,d""" ) {
    val sorted = names.sorted
    val nameScores =  1 to sorted.length map  ( idx => NameScorer.simpleScore(sorted(idx - 1 ), idx))
    assert(nameScores.sum == namesScore)
  }

    test(f"""creating a NameScorer with a list of strings should succeed and have a score4Names of $namesScore%,d""") {
    val scorer = new NameScorer(Right(names), NameScorer.simpleScore)
    assert(scorer.score4Names == namesScore)
  }

  test(f"creating a NameScorer with a File should succeed and have a score4Names of $namesFileScore%,d") {
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
