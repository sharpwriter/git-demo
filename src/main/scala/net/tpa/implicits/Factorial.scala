package net.tpa.implicits
import net.tpa.calculators.Calculators._

sealed trait FactorialGenerator[A] {

  def !(value:A):BigInt

}


object Factorial {

  implicit val IntFactorialGenerator = new FactorialGenerator[Int] {
    override def !(value: Int): BigInt = goodFactorial(value)
  }

  implicit val StringFactorialGenerator = new FactorialGenerator[String] {
    override def !(value: String): BigInt = goodFactorial(value.length)
  }

  implicit val LongFactorialGenerator = new FactorialGenerator[Long] {
    override def !(value: Long): BigInt = goodFactorial(value.toInt)
  }

  def apply[A](implicit factorialGenerator: FactorialGenerator[A]) = factorialGenerator

  implicit class FactorialOps[A: FactorialGenerator](instance:A) {
    def ! = Factorial[A].!(instance)
  }


}
