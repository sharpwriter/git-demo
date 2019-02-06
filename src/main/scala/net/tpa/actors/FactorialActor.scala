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

  override def preStart(): Unit = {
    super.preStart()
    log.info("Info message, should still appear, but not the log.debug below once, log level set to INFO")
  }


  override def receive: Receive = {
    case FactorialRequest(n) =>
      log.debug("Starting to execute the good factorial function")
      sender() ! FactorialResponse(goodFactorial(n))
    case IsFactorialNumberRequest(n) =>

      factorialOf(n) match {
      case Some(number) => sender() ! IsAFactorialNumberFor(number)
      case None => sender() ! NotAFactorialNumber
    }
  }
}
