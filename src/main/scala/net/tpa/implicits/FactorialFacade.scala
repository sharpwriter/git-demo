package net.tpa.implicits


object FactorialFacade  {

  def calculateFactorial[A](value:A)(implicit generator: FactorialGenerator[A]): BigInt = {
    generator.!(value)
  }

}


