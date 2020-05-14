package com.webfarm.Scorinator

import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import java.io.File

import com.github.tototoshi.csv._

import scala.annotation.tailrec

/**
 *
 * @param nameInput takes Either a java.io.File or a List[String] for input
 * @param scoreFn   (String, Int) => Int to transform a String with a specific index to a numeric score
 */
class NameScorer(nameInput: Either[File, List[String]], normalizer: String => String, scoreFn: (String, Int) => Int) {

  import NameScorer._

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
  private final def _scoreList(names: List[String], index: Int, accumulatedScore: BigInt): BigInt = names match {
    case Nil => accumulatedScore
    case n :: ns => _scoreList(ns, index + 1, accumulatedScore + scoreFn(normalizer(n), index))
  }

  lazy val score4Names = _scoreList(names2score, 1, BigInt(0))

}

object NameScorer {
  val logger = LoggerFactory.getLogger(classOf[NameScorer])

  val simpleNormalizer: String => String = (name: String) => {
    name.replaceAll("[^A-Za-z]","").toUpperCase()
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
