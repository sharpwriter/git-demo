package net.tpa.actors

import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill, Props, Terminated}

//#greeter-companion
//#greeter-messages
object Greeter {
  //#greeter-messages
  def props(message: String, printerActor: ActorRef): Props = Props(new Greeter(message, printerActor))
  //def props(message: String, printerActor: ActorRef): Props = Props(classOf[Greeter], message, printerActor)
  //#greeter-messages
  final case class WhoToGreet(who: String)
  case object Greet
}
//#greeter-messages
//#greeter-companion

//#greeter-actor
class Greeter(message: String, printerActor: ActorRef) extends Actor with ActorLogging {
  import Greeter._
  import Printer._

  var greeting = ""

  override def preStart() {
    super.preStart()
   log.debug(s"Greeter ${self.path} is starting to watch over the printer reference : ${printerActor.path}")
    context.watch(printerActor)
  }

  override def postStop(): Unit = {
    log.debug(s"Greeter ${self.path} is shutting down.")
  }

  def receive : Receive = {
    case WhoToGreet(who) =>
      greeting = message + ", " + who
    case Greet           =>
      //#greeter-send-message
      printerActor ! Greeting(greeting)
    //#greeter-send-message
    case Terminated(actor) => if(actor == printerActor) self ! PoisonPill

  }
}
//#greeter-actor
