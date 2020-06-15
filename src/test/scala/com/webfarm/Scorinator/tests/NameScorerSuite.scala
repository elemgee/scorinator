package com.webfarm.Scorinator.tests


import org.slf4j.LoggerFactory
import org.scalatest.funsuite.AnyFunSuite
import java.io.File

import com.webfarm.Scorinator._
import org.scalatest.Ignore


class NameScorerSuite extends AnyFunSuite {

  import NameScorerSuite._

  test(s""""${linda}" in sort position ${lindaOrder} should receive a score of ${lindaScore}""") {
    val (lscore, t1) = time{NameScorer.simpleScore(linda, lindaOrder)}
    assert( lscore == lindaScore)
  }

  test(s""""${linda}" should be at sort position ${lindaOrder} (array index ${lindaOrder - 1}) in the test array""") {
    val sorted = names.sorted
    assert(linda.equals(sorted(lindaOrder - 1)))
  }

  test(f"""the total score for the test list be $namesScore%,d""") {
    val sorted = names.sorted
    val (nameScores,t1) = time{(1 to sorted.length map (idx => NameScorer.simpleScore(sorted(idx - 1), idx))).sum}
    info(f"""mapping for NameScorer.simpleScore  took $t1%,d""")
    assert(nameScores == namesScore)
  }

  test(f"""the total score for the small test list (using the reduceLeft version) should be $namesScore%,d""") {
    val sorted = names.sorted
    val (reducedScore,t2) = time{NameScorer.scoreListByReducer(sorted)}
    info(f"""reduceLeft for sorted small names test took $t2%,d""")
    assert(reducedScore == namesScore)
  }

  test(f"""the total score for the test list from the file  using the NameScorer class instance  should be $namesFileScore%,d""") {
    val (reducedScore,t3) = time{new NameScorer(Right(NameScorer.nameListFromFile(new File(testfile.toURI)).sorted), NameScorer.simpleNormalizer, NameScorer.simpleScore).score4Names}
    info(f"""NameScorer class usage for sorted test list  test took $t3%,d""")
    assert(reducedScore == namesFileScore)
  }

  test(f"""the total score for the  test list from the file(using the reduceLeft version) should be $namesFileScore%,d""") {
    val sorted = NameScorer.nameListFromFile(new File(testfile.toURI)).sorted
    val (reducedScore,t3) = time{NameScorer.scoreListByReducer(sorted)}
    info(f"""reduceLeft for sorted test list  test took $t3%,d""")
    assert(reducedScore == namesFileScore)
  }

  test("""mixed case names should be normalized to all uppercase, no spaces """) {
    info(s"$n1 should be normalized to $target")
    assert(target.contentEquals(NameScorer.simpleNormalizer(n1)))
    info(s"The score should be the same for $n1 and $n2 given the same sort index")
    val s1 = NameScorer.simpleScore(NameScorer.simpleNormalizer(n1), 1)
    val s2 = NameScorer.simpleScore(NameScorer.simpleNormalizer(n2), 1)
    assert(s1 == s2)
  }

  test("test with non-alpha characters and spaces will match clean score") {
    info(s"""testing "$dirty1"""")
    assert(target.contentEquals(NameScorer.simpleNormalizer(dirty1)))
    val s1 = NameScorer.simpleScore(NameScorer.simpleNormalizer(n1), 1)
    val s2 = NameScorer.simpleScore(NameScorer.simpleNormalizer(dirty1), 1)
    assert(s1 == s2)
  }

  test(f"creating a NameScorer should succeed") {
    info(f"check NameScorer using small test list as input should have a score4Names of $namesScore%,d")
    val testScorer = new NameScorer(Right(names), NameScorer.simpleNormalizer, NameScorer.simpleScore)
    assert(testScorer.score4Names == namesScore)
    info(s"did we score all the names for the small test list? (${testScorer.scoredNames.head})")
    assert(testScorer.scoredNames.size == names.size)
    info(f"check NameScorer using a java.io.File as input should have a score4Names of $namesFileScore%,d")
    val fileScorer = new NameScorer(Left(new File(testfile.toURI)), NameScorer.simpleNormalizer, NameScorer.simpleScore)
    assert(fileScorer.score4Names == namesFileScore)
    info(s"did we score all the names for the java.io.File? (${fileScorer.scoredNames.head})")
    assert(fileScorer.scoredNames.size == fileScorer.names2score.size)
    info(f"check NameScorer using a List[String] as input should have a score4Names of $namesFileScore%,d")
    val nameListFromFile = NameScorer.nameListFromFile(new File(testfile.toURI))
    val listScorer = new NameScorer(Right(nameListFromFile), NameScorer.simpleNormalizer, NameScorer.simpleScore)
    assert(listScorer.score4Names == namesFileScore)
    info(s"did we score all the names for the List[String]? (${listScorer.scoredNames.head})")
    assert(listScorer.scoredNames.size == nameListFromFile.size)
  }


}


class NameScorerStressSuite extends AnyFunSuite {

  import NameScorerSuite._

  val nameListFromFile = NameScorer.nameListFromFile(new File(testfile.toURI))

  stressme foreach { stressIterations =>
    val bigList = List.fill(stressIterations)(nameListFromFile.sorted).flatten
    val scorer = new NameScorer(Right(bigList), NameScorer.simpleNormalizer, NameScorer.simpleScore)
    val bigscore = scorer.score4Names
    info(s"using the test file to create a very large list, see if we can handle ${stressIterations} times the data")
      test(f"""The size of the big list should be ${nameListFromFile.size}%,d x $stressIterations (${nameListFromFile.size * stressIterations}%,d) """) {
       if ( stressIterations > 1) {
         assert(bigList.size == nameListFromFile.size * stressIterations)
       }  else {
         assert(bigList.size == nameListFromFile.size * stressIterations)
       }
      }
      test(f"""The overall score for the big list should calculate cleanly and should be at least ${namesFileScore}%,d ($bigscore%,d)""") {
        if ( stressIterations > 1) {
          assert(namesFileScore < bigscore)
        } else {
          assert(namesFileScore == bigscore)
        }
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

  val n1 = "Larry Mills-Gahl"
  val n2 = "Mills-Gahl, Larry"
  val target = "LARRYMILLSGAHL"

  val dirty1 = "Lar  ry Mi@lls-G#ahl)([]"

  val namesFileScore = BigInt(871198282)
  val stressme = 10 to 20 by 4

  def time[R](block: => R): (R,Long) = {
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    println("Elapsed time: " + (t1 - t0) + "ns")
    (result, (System.nanoTime() - t0))
  }

}
