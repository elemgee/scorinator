package com.webfarm.Scorinator

/** examples of additional types of normalizers, mappings, and scoring algorithms
 * There is nothing special or interesting here except to exercise the possibility
 * of using different types of normalizers or composing normalizers (or rather,
 * decomposing the alpha only uppercase normalizer used in the primary exercise)
 *
 */
object ExtendedScorer {

  /**
   * eliminates all non alpha characters and
   * uppercase all characters in the string
   */
  val uppercaseNormalizer: String => String = (name: String) => {
    alphaNormalizer(name).toUpperCase()
  }


  /** eliminates all non-alpha characters */
  val alphaNormalizer: String => String = (name: String) => {
    name.replaceAll("[^A-Za-z]", "")
  }

  /**
   * construct a map of characters to character scores where
   * {A = 1, B = 2, C = 3 ... Y = 25, Z = 26, a = 27... z = 52}
   */
  lazy val alphaMap: Map[Char, Int] = {
    val chars = ('A' to 'Z').toList ::: ('a' to 'z').toList
    chars.map(c => c -> (chars.indexOf(c) + 1)).toMap
  }

  /**
   * as you may have guessed, this is a reverse alpha map
   */
  lazy val reverseAlphaMap: Map[Char, Int] = {
    val chars = (('A' to 'Z').toList ::: ('a' to 'z').toList).reverse
    chars.map(c => c -> (chars.indexOf(c) + 1)).toMap
  }


  /**
   * alpha map including numbers
   */
  lazy val alphaNumericMap: Map[Char, Int] = {
    val chars = ('A' to 'Z').toList ::: ('a' to 'z').toList ::: ('0' to '9').toList
    chars.map(c => c -> (chars.indexOf(c) + 1)).toMap
  }


  /**
   * Simple score summing the characters and multiplying by the sortPosition
   * @param mapping
   * @param name
   * @param sortPosition
   * @return
   */
  def sumScore(mapping: Map[Char, Int])(name: String, sortPosition: Int) = {
    val chars = name.toList.map(c => mapping.getOrElse(c, 0))
    chars.sum * sortPosition
  }

  /** An absolutely arbitrary scoring algorithm demonstrating that given a normal
   * set of inputs, the score can be whatever is appropriate for the context.
   *
   * I'm just coercing this to an intValue because we're looking for integer
   * scores here. Nothing says the score has to be an Int (or a Double...
   * true/false, ...). Int is just the context of this exercise
   */
  def varianceScore(mapping: Map[Char, Int])(name: String, sortPosition: Int) = {
    val charscores = name.toList.map(c => mapping.getOrElse(c, 0).doubleValue())
    (variance(charscores) * 100).intValue()
  }

  val alphaNumVarianceScore = varianceScore(alphaNumericMap)(_, _)

  /**
   * this should be a recreation of the NameScorer.simpleScorer
   */
  val alphaMapSumScore = sumScore(alphaMap)(_, _)

  private def variance(xs: List[Double]) = {
    val localmean = xs.sum / xs.length

    @scala.annotation.tailrec
    def _sumOfSquares(nums: List[Double], total: Double): Double = nums match {
      case Nil => {
        total
      }
      case x :: xs => {
        _sumOfSquares(xs, total + math.pow(x - localmean, 2))
      }
    }

    _sumOfSquares(xs, 0) / (xs.length)
  }
}
