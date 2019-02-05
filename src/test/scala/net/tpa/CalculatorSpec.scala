package net.tpa

import net.tpa.calculators.Calculators.{factorialOf, goodFactorial}
import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{Matchers, WordSpec}

class CalculatorSpec extends WordSpec with Matchers with GeneratorDrivenPropertyChecks {

"Calculator" when {
  "given an input for the GoodFactorial" should {
    "return the same value when piping the result into the factorialOf fn" in {
        forAll(Gen.posNum[Int]) { number:Int =>
          Some(number) shouldEqual(factorialOf(goodFactorial(number)))
        }
    }
  }
}


}
