package net.tpa.actors

import akka.actor.{Actor, ActorLogging, PoisonPill, Props}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
//#printer-companion
//#printer-messages
object Printer {
  //#printer-messages
  def props: Props = Props[Printer]
  //#printer-messages
  case class Greeting(greeting: String)

  case object Shutdown
}
//#printer-messages
//#printer-companion

//#printer-actor
class Printer extends Actor with ActorLogging {
  import Printer._



  override def preStart() {
    implicit val ec = context.system.dispatcher
    context.system.scheduler.scheduleOnce(30.second, self, Shutdown)
  }


  def receive : Receive = {
    case Greeting(greeting) =>
      log.debug("Greeting received (from " + sender() + "): " + greeting)
    case Shutdown =>
      log.debug("received Shutdown message... Sending myself the poison pill")
      self ! PoisonPill
  }
}
//#printer-actor

