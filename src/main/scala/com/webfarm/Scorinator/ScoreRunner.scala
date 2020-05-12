package com.webfarm.Scorinator

<<<<<<< HEAD
import org.slf4j.LoggerFactory
import java.io.{File, FileNotFoundException}

object ScoreRunner {
  val logger = LoggerFactory.getLogger("ScoreRunner")
=======
import com.typesafe.scalalogging.Logger

import java.io.File

object ScoreRunner {
  val logger = Logger("ScoreRunner")
>>>>>>> stubs for initial thoughts and documentation of expectations

  def main(args: Array[String]): Unit = {
    logger debug "initializing ScoreRunner"
    try {
<<<<<<< HEAD
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
=======
      val fname = args(0)
      val file2score = new File(fname)
      logger debug file2score.exists().toString


    } catch {
      catch bounds: ArrayIndexOutOfBoundsException => {
        logger error "You must pass the filename (with complete path) as an argument to ScoreRunner"
        logger error bounds.getCause.toString
>>>>>>> stubs for initial thoughts and documentation of expectations
      }
      case t: Throwable => {
        t.printStackTrace()

      }
    }

    logger debug "finished ScoreRunner"
  }

<<<<<<< HEAD

  lazy val usage =
    s"""
  =========
  ScoreRunner

  This is a command line wrapper to calculate and sum scores for a list of names.
  Pass in a file with a comma separated list of names to be scored

  Usage: java -jar scorinator.jar FILE_TO_SCORE


      """.stripMargin

}

=======
}
>>>>>>> stubs for initial thoughts and documentation of expectations
