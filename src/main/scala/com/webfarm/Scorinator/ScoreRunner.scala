package com.webfarm.Scorinator

import org.slf4j.LoggerFactory
import java.io.{File, FileNotFoundException}

/**
 * utility to take command line input and run the name scorer for a file
 *
 *
 */
object ScoreRunner {
  val logger = LoggerFactory.getLogger("ScoreRunner")

  /**
   * This is a bit overkill, but I pulled it out here so that it
   * could be tested to throw the right exception given bad input
   *
   * @param args
   * @return file to be processed
   */
  def fileFromCommandLine(args: Array[String]): File = {
    new File(args(0))
  }

  /**
   *
   * @param args args[0] is the file to be processed
   */
  def main(args: Array[String]): Unit = {
    logger debug "initializing ScoreRunner"
    try {
      val f = fileFromCommandLine(args)
      val scorer = new NameScorer(Left(f), NameScorer.simpleNormalizer, NameScorer.simpleScore)
      val filescore = scorer.score4Names
      println(f"""\nThe total score for names in ${f.getName} is ${filescore}%,d""")
      println(
        f"""
           |     file to score: ${f.getAbsolutePath}
           |     names in file: ${scorer.names2score.size}
           |       total score: ${filescore}%,d
           |
           |
           |""".stripMargin)


    } catch {
      case bounds: ArrayIndexOutOfBoundsException => {
        println("You must specify a file containing names to be scored")
        println(usage)
      }
      case fnf: FileNotFoundException => {
        println("The file you passed on the command line could not be found")
        println(fnf.getMessage)
        println(usage)
      }
      case t: Throwable => {
        t.printStackTrace()
      }
    }

    logger debug "finished ScoreRunner"
  }

  /** ScoreRunner
   * This is a command line wrapper to calculate and sum scores for a list of names.
   * Pass in a file with a comma separated list of names to be scored
   *
   * Usage: java -jar scorinator.jar FILE_TO_SCORE
   *
   */

  lazy val usage =
    s"""
  =========
  ScoreRunner

  This is a command line wrapper to calculate and sum scores for a list of names.
  Pass in a file with a comma separated list of names to be scored

  Usage: java -jar scorinator.jar FILE_TO_SCORE


      """.stripMargin

}

