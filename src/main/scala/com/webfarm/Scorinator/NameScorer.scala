package com.webfarm.Scorinator


import org.slf4j.LoggerFactory
import java.io.File

import com.github.tototoshi.csv._

import scala.annotation.tailrec

/** Utility for generating a score from a {@code List[String]}
 * This is intended to exercise some flexibility in handling input and scoring mechanism.
 *
 * The basic input content is a {@code List[String]} but to allow this to more easily be used from
 * the command line as well as within a larger context (like a web app or service), the input can
 * be either a File (that is then converted to a List[String]) or a List[String]
 *
 * @param nameInput  takes Either a java.io.File or a List[String] for input
 * @param normalizer (String) => String function to enforce character policies (all uppercase? non-alpha? include punctuation?)
 * @param scoreFn    (String, Int) => Int to transform a String with a specific index to a numeric score
 */
class NameScorer(nameInput: Either[File, List[String]], normalizer: String => String, scoreFn: (String, Int) => Int) {

  import NameScorer._


  /** input to be scored. Converts File input to List[String] or passes List[String] through. The list is sorted on return
   *
   */
  val names2score = {
    val namelist = nameInput match {
      case Left(f) => {
        logger debug s"""got a file named ${f.getName} to score"""
        nameListFromFile(f)
      }
      case Right(l) => {
        logger debug s"""got a list of names ${l.size} elements long to score"""
        l
      }
    }
    namelist.sorted
  }

  @tailrec
  private final def _scoreList(names: List[String], index: Int, accumulatedScore: BigInt, accumulatedNames: List[ScoredName]): (BigInt, List[ScoredName]) = names match {
    case Nil => (accumulatedScore, accumulatedNames)
    case n :: ns => {
      val nscore = scoreFn(normalizer(n), index)
      _scoreList(ns, index + 1, accumulatedScore + nscore, ScoredName(n, Map("index" -> index), nscore) :: accumulatedNames)
    }
  }

  /** score4Names is the total score for all names in the list
   *
   * scoredNames is  a {@code List[ScoredName]} of all the names scored. This is not necessary, but is useful for
   * inspecting the process and (perhaps) identifying where the outcome isn't what you intended or expected.
   */
  lazy val (score4Names, scoredNames) = _scoreList(names2score, 1, BigInt(0), List[ScoredName]())

}


object NameScorer {
  val logger = LoggerFactory.getLogger(classOf[NameScorer])


  /** example of function to be passed into scorer to normalize input and
   * potentially check for malicious input.
   * For the moment, this just eliminates all non-alpha characters and
   * returns an uppercase version of the string passed in.
   *
   * Any function taking a string and returning a string can be used here
   * so decisions about what characters are legal can be adjusted more
   * easily.
   *
   */
  val simpleNormalizer: String => String = (name: String) => {
    name.replaceAll("[^A-Za-z]", "").toUpperCase()
  }

  /**
   * simpleScore assumes that we're score an upper case version of the name
   * Since we're dealing with character values begining with 1, I'm  subtracting
   * the one less than the int value of 'A' (so I don't have to remember what it is)
   * so we can get to a value for the character of 'A' of 1
   *
   * If some other mapping is desired, this could be modified to accept an arbitrary
   * mapping like Map[Char,Int]
   */
  val simpleScore: (String, Int) => Int = (name: String, sortPosition: Int) => {
    val chars = name.toList.map(c => (c.toInt - ('A'.toInt - 1)))
    chars.sum * sortPosition
  }

  /** alternate to the per-name scorer {@link NameScorer.simpleScore} using reduceLeft
   * to score the entire list in one call
   */
  val scoreListByReducer: (List[String]) => BigInt = (list2score: List[String]) => {
    val a = list2score.map(n => BigInt(n.toList.map(c => (c.toInt - ('A'.toInt - 1))).sum * (list2score.indexOf(n) + 1)))
      a.reduceLeft(_ + _ )
  }


  /**
   * could easily be done inline, but was pulled out for testability
   *
   * @param f : File (assumed to be a comma separated list of strings
   * @return a List[String] from the contents of the file
   */
  def nameListFromFile(f: File): List[String] = {
    val csv = CSVReader.open(f)
    csv.all().flatten
  }
}
