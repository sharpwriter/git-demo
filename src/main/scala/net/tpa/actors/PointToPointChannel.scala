package net.tpa.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

object PointToPointChannel {
  case object UnregistrationOk
  //#singleton-message-classes
  case object RegisterConsumer
  case object UnregisterConsumer
  case object RegistrationOk
  case object UnexpectedRegistration
  case object UnexpectedUnregistration
  case object Reset
  case object ResetOk

  def props: Props = Props[PointToPointChannel]
}


class PointToPointChannel extends Actor with ActorLogging {

  import PointToPointChannel._

  def receive = idle

  def idle: Receive = {
    case RegisterConsumer ⇒
      log.info("RegisterConsumer: [{}]", sender().path)
      sender() ! RegistrationOk
      context.become(active(sender()))
    case UnregisterConsumer ⇒
      log.info("UnexpectedUnregistration: [{}]", sender().path)
      sender() ! UnexpectedUnregistration
      context stop self
    case Reset ⇒ sender() ! ResetOk
    case msg   ⇒ // no consumer, drop
  }

  def active(consumer: ActorRef): Receive = {
    case UnregisterConsumer if sender() == consumer ⇒
      log.info("UnregistrationOk: [{}]", sender().path)
      sender() ! UnregistrationOk
      context.become(idle)
    case UnregisterConsumer ⇒
      log.info("UnexpectedUnregistration: [{}], expected [{}]", sender().path, consumer.path)
      sender() ! UnexpectedUnregistration
      context stop self
    case RegisterConsumer ⇒
      log.info("Unexpected RegisterConsumer [{}], active consumer [{}]", sender().path, consumer.path)
      sender() ! UnexpectedRegistration
      context stop self
    case Reset ⇒
      context.become(idle)
      sender() ! ResetOk
    case msg ⇒ consumer ! msg
  }

}
