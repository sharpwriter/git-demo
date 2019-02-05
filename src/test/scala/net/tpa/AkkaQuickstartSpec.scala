//#full-example
package net.tpa

import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import net.tpa.actors.Greeter
import net.tpa.actors.Greeter.{Greet, WhoToGreet}
import net.tpa.actors.Printer.Greeting

import scala.concurrent.duration._
import scala.language.postfixOps
import net.tpa.calculators.Calculators._


//#test-classes
class AkkaQuickstartSpec(_system: ActorSystem)
  extends TestKit(_system)
  with Matchers
  with WordSpecLike
  with BeforeAndAfterAll {
  //#test-classes

  def this() = this(ActorSystem("AkkaQuickstartSpec"))

  override def afterAll: Unit = {
    shutdown(system)
  }

  //#first-test
  //#specification-example
  "A Greeter Actor" should {

    "pass on a greeting message when instructed to" in {
      //#specification-example
      val testProbe = TestProbe()
      val helloGreetingMessage = "hello"
      val helloGreeter = system.actorOf(Greeter.props(helloGreetingMessage, testProbe.ref))
      val greetPerson = "Akka"
      helloGreeter ! WhoToGreet(greetPerson)
      helloGreeter ! Greet
      testProbe.expectMsg(500 millis, Greeting(helloGreetingMessage + ", " + greetPerson))
    }
  }
  //#first-test
  "Calculator" should {

    "be able to revert the factorial function call by invoking factorialOf in sequence" in {

        val inputValue = 289
        Some(inputValue) shouldEqual(factorialOf(goodFactorial(inputValue)))


    }

  }
}
//#full-example
