package net.tpa.calculators

import scala.annotation.tailrec

object Calculators {


  def goodFactorial(number: Int): BigInt = {

    @tailrec
    def recurse(multiplier:Int, accumulated: BigInt): BigInt = multiplier match {
      case 1 => accumulated
      case _ => recurse(multiplier - 1, accumulated * multiplier)
    }

    recurse(number, 1)

  }

  // write a function that takes in any number (BigInt)
  // and returns you either nothing or the number this input number is the factorial
  // dividend
  // divisor
  // remainder must be 0
  // when the divident = divisor and remainder = 0, quotient = 1

  def factorialOf(number: BigInt):Option[Int] = {

    @tailrec
    def recurse(dividend:BigInt, divisor:Int):Option[Int] = dividend % divisor match {
      case x:BigInt if x == 0 && BigInt(divisor) == dividend => Some(divisor)
      case y:BigInt if y == 0 => recurse(dividend/divisor, divisor + 1)
      case _ => None
    }

    recurse(number, 1)
  }




  // this is going to throw a stack overflow !!!! BAD
  def badFactorial(number : Int ): BigInt = number match {
    case 1 => 1
    case _ => number * badFactorial(number - 1)
  }

}
