package com.webfarm.Scorinator.tests

import java.io.File

import com.webfarm.Scorinator.{ExtendedScorer, NameScorer}
import com.webfarm.Scorinator.tests.NameScorerSuite._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

class ExtendedScorerSuite extends AnyFunSuite with Matchers {

  test(s"$n1 should be normalized to $target") {
    assert(target.contentEquals(ExtendedScorer.uppercaseNormalizer(n1)))
  }

  test(s""""${linda}" in sort position ${lindaOrder} should receive a score of ${lindaScore}""") {
    assert(ExtendedScorer.alphaMapSumScore(linda, lindaOrder) == lindaScore)
  }

  test(f"""the total score for the test list be $namesScore%,d""") {
    val sorted = names.sorted
    val nameScores = 1 to sorted.length map (idx => ExtendedScorer.alphaMapSumScore(sorted(idx - 1), idx))
    assert(nameScores.sum == namesScore)
  }

  test(f"check NameScorer using arbitrary map") {
    info(f"check NameScorer using small test list as input should have a score4Names of $namesScore%,d")
    val testScorer = new NameScorer(Right(names), ExtendedScorer.uppercaseNormalizer, ExtendedScorer.alphaMapSumScore)
    assert(testScorer.score4Names == namesScore)
    info(f"check NameScorer using a java.io.File as input should have a score4Names of $namesFileScore%,d")
    val fileScorer = new NameScorer(Left(new File(testfile.toURI)), ExtendedScorer.uppercaseNormalizer, ExtendedScorer.alphaMapSumScore)
    assert(fileScorer.score4Names == namesFileScore)
    info(f"check NameScorer using a List[String] as input should have a score4Names of $namesFileScore%,d")
    val nameListFromFile = NameScorer.nameListFromFile(new File(testfile.toURI))
    val listScorer = new NameScorer(Right(nameListFromFile), ExtendedScorer.uppercaseNormalizer, ExtendedScorer.alphaMapSumScore)
    assert(listScorer.score4Names == namesFileScore)
  }

}
