package net.tpa.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
//#singleton-message-classes
object Consumer {
  case object End
  case object GetCurrent
  case object Ping
  case object Pong
}
//#singleton-message-classes

/**
  * The Singleton actor
  */
class Consumer(queue: ActorRef, delegateTo: ActorRef) extends Actor with ActorLogging {

  import Consumer._
  import net.tpa.actors.PointToPointChannel._

  var current = 0
  var stoppedBeforeUnregistration = true

  override def preStart(): Unit = queue ! RegisterConsumer

  override def postStop(): Unit = {
    if (stoppedBeforeUnregistration)
      log.warning("Stopped before unregistration")
  }

  def receive = {
    case n: Int if n <= current ⇒
      context.stop(self)
    case n: Int ⇒
      current = n
      delegateTo ! n
    case message @ (RegistrationOk | UnexpectedRegistration) ⇒
      delegateTo ! message
    case GetCurrent ⇒
      sender() ! current
    //#consumer-end
    case End ⇒
      queue ! UnregisterConsumer
    case UnregistrationOk ⇒
      stoppedBeforeUnregistration = false
      context stop self
    case Ping ⇒
      sender() ! Pong
    //#consumer-end
  }
}
