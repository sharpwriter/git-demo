package net.tpa.actors

import akka.actor.{Actor, ActorLogging, Props}
import net.tpa.calculators.Calculators._

object FactorialActor {

  def props():Props = Props(new FactorialActor())

  case class FactorialRequest(number:Int)
  case class FactorialResponse(factorial:BigInt)

  case class IsFactorialNumberRequest(number:BigInt)
  case object NotAFactorialNumber
  case class IsAFactorialNumberFor(number: Int)

}


class FactorialActor extends Actor with ActorLogging {

  import FactorialActor._

  override def receive: Receive = {
    case FactorialRequest(n) =>
      sender() ! FactorialResponse(goodFactorial(n))
    case IsFactorialNumberRequest(n) => factorialOf(n) match {
      case Some(number) => sender() ! IsAFactorialNumberFor(number)
    }
  }
}
