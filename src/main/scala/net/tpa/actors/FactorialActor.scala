package net.tpa.actors
import akka.actor.{Actor, ActorLogging, Props}
import net.tpa.interfaces.IFactorialService

object FactorialActor {

  def props(service:IFactorialService):Props = Props(classOf[FactorialActor], service)

  case class FactorialRequest(number:Int)
  case class FactorialResponse(factorial:BigInt)

  case class IsFactorialNumberRequest(number:BigInt)
  case object NotAFactorialNumber
  case class IsAFactorialNumberFor(number: Int)

}


class FactorialActor(service:IFactorialService) extends Actor with ActorLogging {

  import FactorialActor._

  override def preStart(): Unit = {
    super.preStart()
    log.info("Info message, should still appear, but not the log.debug below once, log level set to INFO")
  }


  override def receive: Receive = {
    case FactorialRequest(n) =>
      log.debug("Starting to execute the good factorial function")
      sender() ! FactorialResponse(service.factorial(n))
    case IsFactorialNumberRequest(n) =>

      service.isFactorialNumber(n) match {
      case Some(number) => sender() ! service.isFactorialNumber(number)
      case None => sender() ! NotAFactorialNumber
    }
  }
}
