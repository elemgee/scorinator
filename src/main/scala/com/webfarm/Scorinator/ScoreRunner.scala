package com.webfarm.Scorinator

import org.slf4j.LoggerFactory

import java.io.File

object ScoreRunner {
  val logger = LoggerFactory.getLogger("ScoreRunner")

  def main(args: Array[String]): Unit = {
    logger debug "initializing ScoreRunner"
    try {
      val fname = args(0)
      val file2score = new File(fname)
      val scorer = new NameScorer(Left(file2score), NameScorer.simpleScore)


    } catch {
      case bounds: ArrayIndexOutOfBoundsException => {
        logger error "You must pass the filename (with complete path) as an argument to ScoreRunner"
        logger error bounds.getCause.toString
      }
      case t: Throwable => {
        t.printStackTrace()

      }
    }

    logger debug "finished ScoreRunner"
  }

}

