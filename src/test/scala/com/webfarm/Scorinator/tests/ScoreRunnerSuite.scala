package com.webfarm.Scorinator.tests


import org.scalatest.funsuite.AnyFunSuite
import java.io.{File, FileNotFoundException}

import com.webfarm.Scorinator.{NameScorer, ScoreRunner}

class ScoreRunnerSuite extends AnyFunSuite {

  test ("No args passed in will throw an IndexOutOfBoundsException when trying to grab the file") {
    assertThrows[IndexOutOfBoundsException] { ScoreRunner.fileFromCommandLine(Array[String]())}
  }
  test ("Non existant file passed in will throw an FileNotFoundException when trying to grab the file") {
    info("this is really testing the NameScorer but it's part of the runner, so I stuck it here too")
    assertThrows[FileNotFoundException] { new NameScorer(Left(new File("NONEXISTANT")), NameScorer.simpleNormalizer,NameScorer.simpleScore)}
  }
}

object ScoreRunnerSuite {}
