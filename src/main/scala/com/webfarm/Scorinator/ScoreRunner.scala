package com.webfarm.Scorinator

import com.typesafe.scalalogging.Logger

import java.io.File

object ScoreRunner {
  val logger = Logger("ScoreRunner")

  def main(args: Array[String]): Unit = {
    logger debug "initializing ScoreRunner"
    try {
      val fname = args(0)
      val file2score = new File(fname)
      logger debug file2score.exists().toString


    } catch {
      catch bounds: ArrayIndexOutOfBoundsException => {
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
