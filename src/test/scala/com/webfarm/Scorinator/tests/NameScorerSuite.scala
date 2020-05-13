package com.webfarm.Scorinator.tests


import org.slf4j.LoggerFactory
import org.scalatest.funsuite.AnyFunSuite


import java.io.File
import com.webfarm.Scorinator._

class NameScorerSuite extends AnyFunSuite {

  import NameScorerSuite._

  test(s""""${linda}" in sort position ${lindaOrder} should receive a score of ${lindaScore}""") {
    assert(NameScorer.simpleScore(linda, lindaOrder) == lindaScore)
  }

  test(s""""${linda}" should be at sort position ${lindaOrder} (array index ${lindaOrder - 1}) in the test array""") {
    val sorted = names.sorted
    assert(linda.equals(sorted(lindaOrder - 1)))
  }

  test(f"""the total score for the test list be $namesScore%,d""") {
    val sorted = names.sorted
    val nameScores = 1 to sorted.length map (idx => NameScorer.simpleScore(sorted(idx - 1), idx))
    assert(nameScores.sum == namesScore)
  }

  test(f"creating a NameScorer should succeed and have a score4Names of $namesFileScore%,d") {

    info("check NameScorer using a java.io.File as input")
    val fileScorer = new NameScorer(Left(new File(testfile.toURI)), NameScorer.simpleScore)
    assert(fileScorer.score4Names == namesFileScore)
    info("check NameScorer using a List[String] as input")
    val nameListFromFile = NameScorer.nameListFromFile(new File(testfile.toURI))
    val listScorer = new NameScorer(Right(nameListFromFile), NameScorer.simpleScore)
    assert(listScorer.score4Names == namesFileScore)
  }


}

class NameScorerStressSuite extends AnyFunSuite {

  import NameScorerSuite._

  val nameListFromFile = NameScorer.nameListFromFile(new File(testfile.toURI))

  stressme foreach { stressIterations =>
    test(s"using the test file to create a very large list, see if we can handle ${stressIterations} times the data") {
      val bigList = List.fill(stressIterations)(nameListFromFile).flatten
      info(f"""The size of the big list should be ${nameListFromFile.size}%,d x $stressIterations (${nameListFromFile.size * stressIterations}%,d) """)
      assert(bigList.size == nameListFromFile.size * stressIterations)
      val scorer = new NameScorer(Right(bigList), NameScorer.simpleScore)
      val bigscore = scorer.score4Names
      info(f"""The overall score for the big list should calculate cleanly and should be greater than ${namesFileScore}%,d ($bigscore%,d)""")
      assert(namesFileScore < bigscore)
    }
  }

}

object NameScorerSuite {

  val logger = LoggerFactory.getLogger(classOf[NameScorerSuite])
  val names = List("MARY", "PATRICIA", "LINDA", "BARBARA", "VINCENZO", "SHON", "LYNWOOD", "JERE", "HAI")
  val testfile = getClass().getResource("/names.txt")
  val linda = "LINDA"
  val lindaOrder = 4
  val lindaScore = 160
  val namesScore = 3194

  val namesFileScore = BigInt(871198282)
  val stressme = 100 to 1000 by 100

}
