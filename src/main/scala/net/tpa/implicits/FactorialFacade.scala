package net.tpa.implicits

import net.tpa.interfaces.IFactorialService
import net.tpa.calculators.Calculators._

object FactorialFacade extends IFactorialService {

  def factorial(n:Int):BigInt = goodFactorial(n)

  def isFactorialNumber(n:BigInt):Option[Int] = isFactorialNumber(n)

}


