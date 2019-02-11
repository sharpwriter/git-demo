package net.tpa.interfaces

trait IFactorialService {

  def factorial(n:Int):BigInt

  def isFactorialNumber(n:BigInt):Option[Int]

}
