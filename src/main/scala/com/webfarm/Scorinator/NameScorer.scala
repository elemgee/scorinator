package com.webfarm.Scorinator

import com.typesafe.config.ConfigFactory
<<<<<<< HEAD
import org.slf4j.LoggerFactory
import java.io.File

import com.github.tototoshi.csv._

import scala.annotation.tailrec

class NameScorer(nameInput: Either[File, List[String]], scoreFn: (String, Int) => Int) {

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
  private final def _scoreList( names: List[String], index: Int, accumulatedScore: BigInt): BigInt = names match {
    case Nil => accumulatedScore
    case n::ns => _scoreList(ns, index + 1, accumulatedScore + scoreFn(n,index))
  }

  val score4Names = _scoreList(names2score,1,BigInt(0))
=======
import com.typesafe.scalalogging.Logger

class NameScorer() {
>>>>>>> stubs for initial thoughts and documentation of expectations

}

object NameScorer {
<<<<<<< HEAD
  val logger = LoggerFactory.getLogger(classOf[NameScorer])

  /**
   * simpleScore assumes that we're score an upper case version of the name
   * Since we're dealing with character values begining with 1, I'm  subtracting
   * the one less than the int value of 'A' (so I don't have to remember what it is)
   * so we can get to a value for the character of 'A' of 1
   */
  val simpleScore: (String, Int) => Int = (name: String, sortPosition: Int) => {
    val chars = name.toUpperCase.toList.map(c => (c.toInt - ('A'.toInt - 1)))
    chars.sum * sortPosition
  }

  def nameListFromFile(f: File): List[String] = {
    val csv = CSVReader.open(f)
    csv.all().flatten
  }
=======
  val logger = Logger(classOf[NameScorer])

  lazy val singleNameConfig = ConfigFactory.load("scorinator.single-name")
  lazy val fmlConfig = ConfigFactory.load("scorinator.last-first-middle")
  lazy val lfmConfig = ConfigFactory.load("first-middle-last")
>>>>>>> stubs for initial thoughts and documentation of expectations
}
