package com.webfarm.Scorinator

import org.slf4j.LoggerFactory
import java.io.{File, FileNotFoundException}

object ScoreRunner {
  val logger = LoggerFactory.getLogger("ScoreRunner")

  def main(args: Array[String]): Unit = {
    logger debug "initializing ScoreRunner"
    try {
      val f = new File(args(0))
      val scorer = new NameScorer(Left(f), NameScorer.simpleScore)
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


  lazy val usage =
    s"""
  =========
  ScoreRunner

  This is a command line wrapper to calculate and sum scores for a list of names.
  Pass in a file with a comma separated list of names to be scored

  Usage: java -jar scorinator.jar FILE_TO_SCORE


      """.stripMargin

}

